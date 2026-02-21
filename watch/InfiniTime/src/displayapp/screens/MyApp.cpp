#include "displayapp/screens/MyApp.h"

#include "displayapp/screens/Symbols.h"
#include "displayapp/InfiniTimeTheme.h"

using namespace Pinetime::Applications::Screens;

MyApp::MyApp() {
  lv_obj_t* title = lv_label_create(lv_scr_act(), nullptr);
  lv_label_set_text_static(title, "Upcoming Doses");
  lv_label_set_align(title, LV_LABEL_ALIGN_CENTER);
  lv_obj_align(title, lv_scr_act(), LV_ALIGN_IN_TOP_MID, 0, 0);
/*
  lv_obj_t* title2 = lv_label_create(lv_scr_act(), nullptr);
  lv_label_set_text_static(title2, "Hepatitis B");
  lv_label_set_align(title2, LV_LABEL_ALIGN_CENTER);
  lv_obj_align(title2, lv_scr_act(), LV_ALIGN_IN_TOP_MID, 0, 30);
*/

  /*Create style*/
  static lv_style_t style_line;
  lv_style_init(&style_line);
  lv_style_set_line_width(&style_line, LV_STATE_DEFAULT, 24);
  lv_style_set_line_color(&style_line, LV_STATE_DEFAULT, LV_COLOR_BLUE);
  lv_style_set_line_rounded(&style_line, LV_STATE_DEFAULT, true);

  /*Create an array for the points of the line*/
  static lv_point_t line_points[] = { {58, 66}, {58, 67} };

  /*Create a line and apply the new style*/
  lv_obj_t * line1;
  line1 = lv_line_create(lv_scr_act(), NULL);
  lv_line_set_points(line1, line_points, 2);     /*Set the points*/
  lv_obj_add_style(line1, LV_LINE_PART_MAIN, &style_line);     /*Set the points*/
  lv_obj_align(line1, NULL, LV_ALIGN_CENTER, 0, 0);

  lv_obj_t  * calendar = lv_calendar_create(lv_scr_act(), nullptr);
  lv_obj_set_size(calendar, 185, 230);
  lv_obj_align(calendar, lv_scr_act(), LV_ALIGN_CENTER, 0, 27);

  lv_calendar_date_t today;
  today.year = 2021;
  today.month = 2;
  today.day = 23;
  lv_calendar_set_today_date(calendar, &today);
  lv_calendar_set_showed_date(calendar, &today);
  
}

MyApp::~MyApp() {
  lv_obj_clean(lv_scr_act());
}