
long FUN_ffff0000907c1ea0(byte *param_1,long param_2)

{
  bool bVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  
  lVar2 = FUN_ffff0000907c0f94(param_2 << 1 | 1);
  if (lVar2 == 0) {
    pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
    lVar3 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
    lVar2 = lVar3 + -2;
    if (-1 < lVar3 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar2] == '/') {
          pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar2 + 1;
          break;
        }
        bVar1 = 0 < lVar2;
        lVar2 = lVar2 + -1;
      } while (bVar1);
    }
    FUN_ffff0000907c0ee8
              (pcVar4,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e23db,s___ERROR__ffff0000907e07dc,
               s_Failed_to_allocate_memory__ffff00009080366c,0);
    lVar2 = 0;
  }
  else {
    if (param_2 == 0) {
      param_2 = 0;
    }
    else {
      pcVar4 = (char *)(lVar2 + 1);
      lVar3 = param_2;
      do {
        lVar3 = lVar3 + -1;
        pcVar4[-1] = s_0123456789abcdef_ffff0000907dea4d[*param_1 >> 4];
        *pcVar4 = s_0123456789abcdef_ffff0000907dea4d[(ulong)*param_1 & 0xf];
        pcVar4 = pcVar4 + 2;
        param_1 = param_1 + 1;
      } while (lVar3 != 0);
      param_2 = param_2 << 1;
    }
    *(undefined1 *)(lVar2 + param_2) = 0;
  }
  return lVar2;
}


