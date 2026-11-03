// ffff0000907c1384  size=448  FUN_ffff0000907c1384

undefined8
FUN_ffff0000907c1384
          (long param_1,long param_2,undefined8 param_3,ulong param_4,undefined8 param_5,
          ulong param_6)

{
  bool bVar1;
  long lVar2;
  undefined *puVar3;
  char *pcVar4;
  long lVar5;
  char *pcVar6;
  
  if (param_2 != 0) {
    if (CARRY8(param_6,param_4)) {
      pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
      lVar2 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      lVar5 = lVar2 + -2;
      if (-1 < lVar2 + -2) {
        do {
          if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/') {
            pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar5 + 1;
            break;
          }
          bVar1 = 0 < lVar5;
          lVar5 = lVar5 + -1;
        } while (bVar1);
      }
      FUN_ffff0000907c0ee8
                (pcVar6,&DAT_ffff0000907ebfa6,&DAT_ffff000090801b7b,s___ERROR__ffff0000907e07dc,
                 s_Overflow_when_adding_values__ffff00009080364e,0);
      pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
      lVar2 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      lVar5 = lVar2 + -2;
      if (-1 < lVar2 + -2) {
        do {
          if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/') {
            pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar5 + 1;
            break;
          }
          bVar1 = 0 < lVar5;
          lVar5 = lVar5 + -1;
        } while (bVar1);
      }
      puVar3 = &DAT_ffff0000907d3c82;
      pcVar4 = s_Overflow_when_adding_string_size_ffff0000907e088f;
    }
    else {
      if (param_6 + param_4 <= param_2 - 1U) {
        thunk_FUN_ffff00009077eb20(param_1,param_3,param_4);
        thunk_FUN_ffff00009077eb20(param_1 + param_4,param_5,param_6);
        *(undefined1 *)(param_1 + param_6 + param_4) = 0;
        return 1;
      }
      pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
      lVar2 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      lVar5 = lVar2 + -2;
      if (-1 < lVar2 + -2) {
        do {
          if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar5] == '/') {
            pcVar6 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar5 + 1;
            break;
          }
          bVar1 = 0 < lVar5;
          lVar5 = lVar5 + -1;
        } while (bVar1);
      }
      puVar3 = &DAT_ffff0000907f0b38;
      pcVar4 = s_Insufficient_buffer_space__ffff0000907d23a1;
    }
    FUN_ffff0000907c0ee8(pcVar6,&DAT_ffff0000907ebfa6,puVar3,s___ERROR__ffff0000907e07dc,pcVar4,0);
  }
  return 0;
}


