// ffff000090796d64  size=148  FUN_ffff000090796d64

void FUN_ffff000090796d64(undefined8 param_1,char param_2,char param_3)

{
  char *pcVar1;
  
  if (param_2 == '%') {
    if (param_3 != '\0') goto LAB_ffff000090796dd8;
    pcVar1 = s__UFS__err__Logical_unit_not_supp_ffff0000907d20f4;
  }
  else if (param_2 == '!') {
    if (param_3 != '\0') goto LAB_ffff000090796dd8;
    pcVar1 = s__UFS__err__LBA_out_of_range__ffff0000907f20c9;
  }
  else {
    if ((param_2 != ')') || (param_3 != '\0')) goto LAB_ffff000090796dd8;
    pcVar1 = s__UFS__warn__Power_on__reset__or_b_ffff0000907dba7e;
  }
  FUN_ffff00009077de54(pcVar1);
LAB_ffff000090796dd8:
  FUN_ffff00009077de54(s__UFS__Sense_Data__ASC__x__ASCQ___ffff0000907fe2e7,param_2,param_3);
  return;
}


