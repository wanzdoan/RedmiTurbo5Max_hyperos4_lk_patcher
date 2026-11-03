// ffff0000907c182c  size=248  FUN_ffff0000907c182c

long FUN_ffff0000907c182c(byte *param_1,long param_2)

{
  bool bVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  
  lVar2 = FUN_ffff0000907c0920(param_2 << 1 | 1);
  if (lVar2 == 0) {
    pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000907d5548;
    lVar3 = thunk_FUN_ffff00009077ef88(s_external_lib_libavb_avb_util_c_ffff0000907d5548);
    lVar2 = lVar3 + -2;
    if (-1 < lVar3 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000907d5548[lVar2] == '/') {
          pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000907d5548 + lVar2 + 1;
          break;
        }
        bVar1 = 0 < lVar2;
        lVar2 = lVar2 + -1;
      } while (bVar1);
    }
    FUN_ffff0000907c0874
              (pcVar4,&DAT_ffff0000907ebe0c,&DAT_ffff0000907e22f3,s___ERROR__ffff0000907e0722,
               s_Failed_to_allocate_memory__ffff000090803444,0);
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
        pcVar4[-1] = s_0123456789abcdef_ffff0000907de993[*param_1 >> 4];
        *pcVar4 = s_0123456789abcdef_ffff0000907de993[(ulong)*param_1 & 0xf];
        pcVar4 = pcVar4 + 2;
        param_1 = param_1 + 1;
      } while (lVar3 != 0);
      param_2 = param_2 << 1;
    }
    *(undefined1 *)(lVar2 + param_2) = 0;
  }
  return lVar2;
}


