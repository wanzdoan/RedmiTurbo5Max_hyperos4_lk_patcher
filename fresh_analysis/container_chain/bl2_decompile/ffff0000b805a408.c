
void FUN_ffff0000b805a408(undefined8 param_1,char param_2,char param_3)

{
  char *pcVar1;
  
  if (param_2 == '%') {
    if (param_3 != '\0') goto LAB_ffff0000b805a47c;
    pcVar1 = s__UFS__err__Logical_unit_not_supp_ffff0000b8086b7e;
  }
  else if (param_2 == '!') {
    if (param_3 != '\0') goto LAB_ffff0000b805a47c;
    pcVar1 = s__UFS__err__LBA_out_of_range__ffff0000b8098797;
  }
  else {
    if ((param_2 != ')') || (param_3 != '\0')) goto LAB_ffff0000b805a47c;
    pcVar1 = s__UFS__warn__Power_on__reset__or_b_ffff0000b808bfad;
  }
  FUN_ffff0000b804c33c(pcVar1);
LAB_ffff0000b805a47c:
  FUN_ffff0000b804c33c(s__UFS__Sense_Data__ASC__x__ASCQ___ffff0000b809ef90,param_2,param_3);
  return;
}


