// ffff0000907c0d00  size=412  FUN_ffff0000907c0d00

undefined8
FUN_ffff0000907c0d00(long param_1,undefined8 param_2,long param_3,long *param_4,char *param_5)

{
  undefined8 uVar1;
  long lVar2;
  undefined *puVar3;
  char *pcVar4;
  long local_28;
  
  local_28 = 0;
  if (*(code **)(param_1 + 0x20) == (code *)0x0) {
LAB_ffff0000907c0dc8:
    if (*param_5 == '\0') {
      lVar2 = FUN_ffff0000907c1544(param_3);
      *param_4 = lVar2;
      if (lVar2 == 0) {
        return 1;
      }
      uVar1 = (**(code **)(param_1 + 0x18))(param_1,param_2,0,param_3,lVar2,&local_28);
      if ((int)uVar1 != 0) {
        if ((int)uVar1 == 1) {
          return uVar1;
        }
        uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        puVar3 = (undefined *)0xffff000090800193;
        goto LAB_ffff0000907c0d78;
      }
      if (local_28 != param_3) {
        uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
        puVar3 = &DAT_ffff00009080364a;
        goto LAB_ffff0000907c0e64;
      }
    }
LAB_ffff0000907c0dd0:
    uVar1 = 0;
  }
  else {
    uVar1 = (**(code **)(param_1 + 0x20))(param_1,param_2,param_3,param_4,&local_28);
    if ((int)uVar1 == 0) {
      if (*param_4 == 0) goto LAB_ffff0000907c0dc8;
      *param_5 = '\x01';
      if (local_28 == param_3) goto LAB_ffff0000907c0dd0;
      uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      puVar3 = &DAT_ffff0000907d0774;
LAB_ffff0000907c0e64:
      pcVar4 = s___Read_incorrect_number_of_bytes_ffff0000907fae6c;
    }
    else {
      if ((int)uVar1 == 1) {
        return uVar1;
      }
      uVar1 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_slot_ver_ffff000090801b7f);
      puVar3 = &DAT_ffff0000907ea47a;
LAB_ffff0000907c0d78:
      pcVar4 = s___Error_loading_data_from_partit_ffff0000907ea47e;
    }
    FUN_ffff0000907c0ee8
              (uVar1,&DAT_ffff0000907ebfa6,puVar3,s___ERROR__ffff0000907e07dc,param_2,pcVar4,0);
    uVar1 = 2;
  }
  return uVar1;
}


