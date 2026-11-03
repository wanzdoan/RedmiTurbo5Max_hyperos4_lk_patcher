
void FUN_ffff0000be074798(undefined8 param_1,char param_2,char param_3)

{
  char *pcVar1;
  
  if (param_2 == '%') {
    if (param_3 != '\0') goto LAB_ffff0000be07480c;
    pcVar1 = s__UFS__err__Logical_unit_not_supp_ffff0000be0a520c;
  }
  else if (param_2 == '!') {
    if (param_3 != '\0') goto LAB_ffff0000be07480c;
    pcVar1 = s__UFS__err__LBA_out_of_range__ffff0000be0bb7ec;
  }
  else {
    if ((param_2 != ')') || (param_3 != '\0')) goto LAB_ffff0000be07480c;
    pcVar1 = s__UFS__warn__Power_on__reset__or_b_ffff0000be0abfdb;
  }
  FUN_ffff0000be05fd98(pcVar1);
LAB_ffff0000be07480c:
  FUN_ffff0000be05fd98(s__UFS__Sense_Data__ASC__x__ASCQ___ffff0000be0c3810,param_2,param_3);
  return;
}


