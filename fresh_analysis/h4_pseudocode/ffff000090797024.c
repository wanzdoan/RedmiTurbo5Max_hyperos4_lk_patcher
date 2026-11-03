
void FUN_ffff000090797024(undefined8 param_1,char param_2,char param_3)

{
  char *pcVar1;
  
  if (param_2 == '%') {
    if (param_3 != '\0') goto LAB_ffff000090797098;
    pcVar1 = s__UFS__err__Logical_unit_not_supp_ffff0000907d2131;
  }
  else if (param_2 == '!') {
    if (param_3 != '\0') goto LAB_ffff000090797098;
    pcVar1 = s__UFS__err__LBA_out_of_range__ffff0000907f2255;
  }
  else {
    if ((param_2 != ')') || (param_3 != '\0')) goto LAB_ffff000090797098;
    pcVar1 = s__UFS__warn__Power_on__reset__or_b_ffff0000907dbb89;
  }
  FUN_ffff00009077e11c(pcVar1);
LAB_ffff000090797098:
  FUN_ffff00009077e11c(s__UFS__Sense_Data__ASC__x__ASCQ___ffff0000907fe48f,param_2,param_3);
  return;
}


