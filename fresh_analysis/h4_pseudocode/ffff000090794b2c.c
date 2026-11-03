
void FUN_ffff000090794b2c
               (undefined8 param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4)

{
  long lVar1;
  
  lVar1 = FUN_ffff00009077d514(0xb8);
  if (lVar1 != 0) {
    FUN_ffff00009077ee50(lVar1,0,0xb8);
    *(undefined4 *)(lVar1 + 0xa8) = param_2;
    *(undefined8 *)(lVar1 + 0xb0) = param_4;
    FUN_ffff00009077c230(lVar1,param_1,0x1000,param_3,0,0,4);
    *(code **)(lVar1 + 0x78) = FUN_ffff000090794d90;
    *(code **)(lVar1 + 0x88) = FUN_ffff000090794e38;
    *(code **)(lVar1 + 0x90) = FUN_ffff000090794ee0;
    *(code **)(lVar1 + 0x98) = FUN_ffff000090795064;
    FUN_ffff00009077c9f0(lVar1);
    FUN_ffff0000907809c4(*(undefined8 *)(lVar1 + 0x18),0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_ufs_ufs_ffff0000907ea26a,0x138,&DAT_ffff0000907f556d);
}


