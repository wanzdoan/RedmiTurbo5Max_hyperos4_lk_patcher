// ffff0000907c101c  size=204  FUN_ffff0000907c101c

long FUN_ffff0000907c101c(undefined8 param_1)

{
  bool bVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  
  lVar2 = thunk_FUN_ffff00009077ef88();
  lVar3 = FUN_ffff0000907c0920(lVar2 + 1);
  if (lVar3 == 0) {
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
    lVar3 = 0;
  }
  else {
    thunk_FUN_ffff00009077e858(lVar3,param_1,lVar2);
    *(undefined1 *)(lVar3 + lVar2) = 0;
  }
  return lVar3;
}


