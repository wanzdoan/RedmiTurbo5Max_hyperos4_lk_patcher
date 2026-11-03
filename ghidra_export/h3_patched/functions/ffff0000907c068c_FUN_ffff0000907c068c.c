// ffff0000907c068c  size=412  FUN_ffff0000907c068c

undefined8
FUN_ffff0000907c068c(long param_1,undefined8 param_2,long param_3,long *param_4,char *param_5)

{
  undefined8 uVar1;
  long lVar2;
  undefined *puVar3;
  char *pcVar4;
  long local_28;
  
  local_28 = 0;
  if (*(code **)(param_1 + 0x20) == (code *)0x0) {
LAB_ffff0000907c0754:
    if (*param_5 == '\0') {
      lVar2 = FUN_ffff0000907c0ed0(param_3);
      *param_4 = lVar2;
      if (lVar2 == 0) {
        return 1;
      }
      uVar1 = (**(code **)(param_1 + 0x18))(param_1,param_2,0,param_3,lVar2,&local_28);
      if ((int)uVar1 != 0) {
        if ((int)uVar1 == 1) {
          return uVar1;
        }
        uVar1 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        puVar3 = (undefined *)0xffff0000907fffeb;
        goto LAB_ffff0000907c0704;
      }
      if (local_28 != param_3) {
        uVar1 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
        puVar3 = &DAT_ffff000090803422;
        goto LAB_ffff0000907c07f0;
      }
    }
LAB_ffff0000907c075c:
    uVar1 = 0;
  }
  else {
    uVar1 = (**(code **)(param_1 + 0x20))(param_1,param_2,param_3,param_4,&local_28);
    if ((int)uVar1 == 0) {
      if (*param_4 == 0) goto LAB_ffff0000907c0754;
      *param_5 = '\x01';
      if (local_28 == param_3) goto LAB_ffff0000907c075c;
      uVar1 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar3 = &DAT_ffff0000907d0755;
LAB_ffff0000907c07f0:
      pcVar4 = s___Read_incorrect_number_of_bytes_ffff0000907facd0;
    }
    else {
      if ((int)uVar1 == 1) {
        return uVar1;
      }
      uVar1 = FUN_ffff0000907c0b68(s_external_lib_libavb_avb_slot_ver_ffff0000908019a3);
      puVar3 = &DAT_ffff0000907ea31f;
LAB_ffff0000907c0704:
      pcVar4 = s___Error_loading_data_from_partit_ffff0000907ea323;
    }
    FUN_ffff0000907c0874
              (uVar1,&DAT_ffff0000907ebe0c,puVar3,s___ERROR__ffff0000907e0722,param_2,pcVar4,0);
    uVar1 = 2;
  }
  return uVar1;
}


