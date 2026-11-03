
int FUN_ffff000090799368(int param_1,long param_2,undefined8 param_3,long param_4,ulong *param_5)

{
  int iVar1;
  long lVar2;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_in____NULL_ffff0000907ebbd5,s_platform_mediatek_mt6991_sboot_c_ffff0000907ebbe0,
               0x20);
  }
  if (param_4 != 0) {
    if (param_5 != (ulong *)0x0) {
      iVar1 = FUN_ffff0000907992b0();
      if (iVar1 == 0) {
        if (*param_5 < *(ulong *)((&DAT_ffff00009090aa78)[param_1] + 0x10)) {
          iVar1 = 6;
          *param_5 = *(ulong *)((&DAT_ffff00009090aa78)[param_1] + 0x10);
        }
        else {
          lVar2 = FUN_ffff00009077d514(0x70);
          if (lVar2 == 0) {
            iVar1 = 0xd;
          }
          else {
            iVar1 = (**(code **)((&DAT_ffff00009090aa78)[param_1] + 0xa8))();
            if (iVar1 == 0) {
              iVar1 = (**(code **)((&DAT_ffff00009090aa78)[param_1] + 0xb0))(lVar2,param_2,param_3);
              if (iVar1 == 0) {
                iVar1 = (**(code **)((&DAT_ffff00009090aa78)[param_1] + 0xb8))(lVar2,param_4);
                *param_5 = *(ulong *)((&DAT_ffff00009090aa78)[param_1] + 0x10);
              }
            }
            FUN_ffff00009079c0d4(lVar2,0x70);
            thunk_FUN_ffff0000907c314c(lVar2);
          }
        }
      }
      return iVar1;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_outlen____NULL_ffff0000907ea33c,s_platform_mediatek_mt6991_sboot_c_ffff0000907ebbe0
               ,0x22);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907991f0
            (s_out____NULL_ffff0000907f9090,s_platform_mediatek_mt6991_sboot_c_ffff0000907ebbe0,0x21
            );
}


