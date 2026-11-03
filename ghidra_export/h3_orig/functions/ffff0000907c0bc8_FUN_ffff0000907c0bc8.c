// ffff0000907c0bc8  size=180  FUN_ffff0000907c0bc8

bool FUN_ffff0000907c0bc8(ulong *param_1,ulong param_2,long param_3)

{
  bool bVar1;
  ulong uVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  
  uVar2 = param_3 + param_2;
  if (param_1 != (ulong *)0x0) {
    *param_1 = uVar2;
  }
  if (uVar2 < param_2) {
    pcVar5 = s_external_lib_libavb_avb_util_c_ffff0000907d5548;
    lVar3 = thunk_FUN_ffff00009077ef88(s_external_lib_libavb_avb_util_c_ffff0000907d5548);
    lVar4 = lVar3 + -2;
    if (-1 < lVar3 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000907d5548[lVar4] == '/') {
          pcVar5 = s_external_lib_libavb_avb_util_c_ffff0000907d5548 + lVar4 + 1;
          break;
        }
        bVar1 = 0 < lVar4;
        lVar4 = lVar4 + -1;
      } while (bVar1);
    }
    FUN_ffff0000907c0874
              (pcVar5,&DAT_ffff0000907ebe23,&DAT_ffff00009080199f,s___ERROR__ffff0000907e0739,
               s_Overflow_when_adding_values__ffff000090803426,0);
  }
  return param_2 <= uVar2;
}


