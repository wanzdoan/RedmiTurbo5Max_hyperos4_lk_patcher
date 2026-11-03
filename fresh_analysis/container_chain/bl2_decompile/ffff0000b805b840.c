
int FUN_ffff0000b805b840(int param_1,long param_2,undefined8 param_3,long param_4,ulong *param_5)

{
  int iVar1;
  long lVar2;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_in____NULL_ffff0000b8094f0a,s_platform_mediatek_mt6991_sboot_c_ffff0000b8094f15,
               0x20);
  }
  if (param_4 != 0) {
    if (param_5 != (ulong *)0x0) {
      iVar1 = FUN_ffff0000b805b788();
      if (iVar1 == 0) {
        if (*param_5 < *(ulong *)((&DAT_ffff0000b81b6cc8)[param_1] + 0x10)) {
          iVar1 = 6;
          *param_5 = *(ulong *)((&DAT_ffff0000b81b6cc8)[param_1] + 0x10);
        }
        else {
          lVar2 = FUN_ffff0000b804bccc(0x70);
          if (lVar2 == 0) {
            iVar1 = 0xd;
          }
          else {
            iVar1 = (**(code **)((&DAT_ffff0000b81b6cc8)[param_1] + 0xa8))();
            if (iVar1 == 0) {
              iVar1 = (**(code **)((&DAT_ffff0000b81b6cc8)[param_1] + 0xb0))(lVar2,param_2,param_3);
              if (iVar1 == 0) {
                iVar1 = (**(code **)((&DAT_ffff0000b81b6cc8)[param_1] + 0xb8))(lVar2,param_4);
                *param_5 = *(ulong *)((&DAT_ffff0000b81b6cc8)[param_1] + 0x10);
              }
            }
            FUN_ffff0000b805e5ac(lVar2,0x70);
            thunk_FUN_ffff0000b8082558(lVar2);
          }
        }
      }
      return iVar1;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_outlen____NULL_ffff0000b8093e9d,s_platform_mediatek_mt6991_sboot_c_ffff0000b8094f15
               ,0x22);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b805b6c8
            (s_out____NULL_ffff0000b809bfca,s_platform_mediatek_mt6991_sboot_c_ffff0000b8094f15,0x21
            );
}


