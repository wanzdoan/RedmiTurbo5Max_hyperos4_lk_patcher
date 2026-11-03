
int FUN_ffff0000be075a30(int param_1,long param_2,undefined8 param_3,long param_4,ulong *param_5)

{
  int iVar1;
  long lVar2;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_in____NULL_ffff0000be0b70a9,s_platform_mediatek_mt6991_sboot_c_ffff0000be0b70b4,
               0x20);
  }
  if (param_4 != 0) {
    if (param_5 != (ulong *)0x0) {
      iVar1 = FUN_ffff0000be075978();
      if (iVar1 == 0) {
        if (*param_5 < *(ulong *)((&DAT_ffff0000be1b4f88)[param_1] + 0x10)) {
          iVar1 = 6;
          *param_5 = *(ulong *)((&DAT_ffff0000be1b4f88)[param_1] + 0x10);
        }
        else {
          lVar2 = FUN_ffff0000be05f1bc(0x70);
          if (lVar2 == 0) {
            iVar1 = 0xd;
          }
          else {
            iVar1 = (**(code **)((&DAT_ffff0000be1b4f88)[param_1] + 0xa8))();
            if (iVar1 == 0) {
              iVar1 = (**(code **)((&DAT_ffff0000be1b4f88)[param_1] + 0xb0))(lVar2,param_2,param_3);
              if (iVar1 == 0) {
                iVar1 = (**(code **)((&DAT_ffff0000be1b4f88)[param_1] + 0xb8))(lVar2,param_4);
                *param_5 = *(ulong *)((&DAT_ffff0000be1b4f88)[param_1] + 0x10);
              }
            }
            FUN_ffff0000be07879c(lVar2,0x70);
            thunk_FUN_ffff0000be09937c(lVar2);
          }
        }
      }
      return iVar1;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_outlen____NULL_ffff0000be0b5de1,s_platform_mediatek_mt6991_sboot_c_ffff0000be0b70b4
               ,0x22);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be0758b8
            (s_out____NULL_ffff0000be0c018a,s_platform_mediatek_mt6991_sboot_c_ffff0000be0b70b4,0x21
            );
}


