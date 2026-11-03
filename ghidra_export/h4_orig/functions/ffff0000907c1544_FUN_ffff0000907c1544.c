// ffff0000907c1544  size=152  FUN_ffff0000907c1544

long FUN_ffff0000907c1544(void)

{
  bool bVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  
  lVar2 = FUN_ffff0000907c0f94();
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
  return lVar2;
}


