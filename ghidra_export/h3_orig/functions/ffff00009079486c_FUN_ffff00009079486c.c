// ffff00009079486c  size=216  FUN_ffff00009079486c

void FUN_ffff00009079486c
               (undefined8 param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4)

{
  long lVar1;
  
  lVar1 = FUN_ffff00009077d24c(0xb8);
  if (lVar1 != 0) {
    FUN_ffff00009077eb88(lVar1,0,0xb8);
    *(undefined4 *)(lVar1 + 0xa8) = param_2;
    *(undefined8 *)(lVar1 + 0xb0) = param_4;
    FUN_ffff00009077bf68(lVar1,param_1,0x1000,param_3,0,0,4);
    *(code **)(lVar1 + 0x78) = FUN_ffff000090794ad0;
    *(code **)(lVar1 + 0x88) = FUN_ffff000090794b78;
    *(code **)(lVar1 + 0x90) = FUN_ffff000090794c20;
    *(code **)(lVar1 + 0x98) = FUN_ffff000090794da4;
    FUN_ffff00009077c728(lVar1);
    FUN_ffff0000907806fc(*(undefined8 *)(lVar1 + 0x18),0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_ufs_ufs_ffff0000907ea126,0x138,&DAT_ffff0000907f5400);
}


