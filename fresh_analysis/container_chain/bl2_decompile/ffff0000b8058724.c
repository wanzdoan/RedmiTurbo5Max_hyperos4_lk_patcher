
void FUN_ffff0000b8058724
               (undefined8 param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4)

{
  long lVar1;
  
  lVar1 = FUN_ffff0000b804bccc(0xb8);
  if (lVar1 != 0) {
    FUN_ffff0000b804d070(lVar1,0,0xb8);
    *(undefined4 *)(lVar1 + 0xa8) = param_2;
    *(undefined8 *)(lVar1 + 0xb0) = param_4;
    FUN_ffff0000b804aba4(lVar1,param_1,0x1000,param_3,0,0,4);
    *(code **)(lVar1 + 0x78) = FUN_ffff0000b8058864;
    *(code **)(lVar1 + 0x88) = FUN_ffff0000b805890c;
    *(code **)(lVar1 + 0x90) = FUN_ffff0000b80589b4;
    *(code **)(lVar1 + 0x98) = FUN_ffff0000b8058b38;
    FUN_ffff0000b804b364(lVar1);
    FUN_ffff0000b804ea14(*(undefined8 *)(lVar1 + 0x18),0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_ufs_ufs_ffff0000b8093dcb,0x138,&DAT_ffff0000b809a30d);
}


