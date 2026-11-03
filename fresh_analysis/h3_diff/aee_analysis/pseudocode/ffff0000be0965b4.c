
long FUN_ffff0000be0965b4(void)

{
  bool bVar1;
  long lVar2;
  long lVar3;
  char *pcVar4;
  
  lVar2 = FUN_ffff0000be096508();
  if (lVar2 == 0) {
    pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000be0a78e7;
    lVar3 = thunk_FUN_ffff0000be060e5c(s_external_lib_libavb_avb_util_c_ffff0000be0a78e7);
    lVar2 = lVar3 + -2;
    if (-1 < lVar3 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000be0a78e7[lVar2] == '/') {
          pcVar4 = s_external_lib_libavb_avb_util_c_ffff0000be0a78e7 + lVar2 + 1;
          break;
        }
        bVar1 = 0 < lVar2;
        lVar2 = lVar2 + -1;
      } while (bVar1);
    }
    FUN_ffff0000be09645c
              (pcVar4,&DAT_ffff0000be0b8378,&DAT_ffff0000be0b0719,s___ERROR__ffff0000be0af2eb,
               s_Failed_to_allocate_memory__ffff0000be0c6de0,0);
    lVar2 = 0;
  }
  return lVar2;
}


