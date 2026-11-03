// ffff0000907990a8  size=356  FUN_ffff0000907990a8

int FUN_ffff0000907990a8(int param_1,long param_2,undefined8 param_3,long param_4,ulong *param_5)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_in____NULL_ffff0000907eba3b,s_platform_mediatek_mt6991_sboot_c_ffff0000907eba46,
               0x20);
  }
  if (param_4 != 0) {
    if (param_5 != (ulong *)0x0) {
      iVar1 = FUN_ffff000090798ff0();
      if (iVar1 == 0) {
        uVar3 = *(ulong *)(*(long *)((long)param_1 * 8 + -0xffff6f6f5648) + 0x10);
        if (*param_5 < uVar3) {
          iVar1 = 6;
          *param_5 = uVar3;
        }
        else {
          lVar2 = FUN_ffff00009077d24c(0x70);
          if (lVar2 == 0) {
            iVar1 = 0xd;
          }
          else {
            lVar4 = (long)param_1;
            iVar1 = (**(code **)(*(long *)(lVar4 * 8 + -0xffff6f6f5648) + 0xa8))();
            if (iVar1 == 0) {
              iVar1 = (**(code **)(*(long *)(lVar4 * 8 + -0xffff6f6f5648) + 0xb0))
                                (lVar2,param_2,param_3);
              if (iVar1 == 0) {
                iVar1 = (**(code **)(*(long *)(lVar4 * 8 + -0xffff6f6f5648) + 0xb8))(lVar2,param_4);
                *param_5 = *(ulong *)(*(long *)(lVar4 * 8 + -0xffff6f6f5648) + 0x10);
              }
            }
            FUN_ffff00009079be14(lVar2,0x70);
            thunk_FUN_ffff0000907c2ad8(lVar2);
          }
        }
      }
      return iVar1;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_outlen____NULL_ffff0000907ea1e1,s_platform_mediatek_mt6991_sboot_c_ffff0000907eba46
               ,0x22);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff000090798f30
            (s_out____NULL_ffff0000907f8ef4,s_platform_mediatek_mt6991_sboot_c_ffff0000907eba46,0x21
            );
}


