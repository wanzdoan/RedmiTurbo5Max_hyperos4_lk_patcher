
void FUN_ffff0000be0724b0
               (undefined8 param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4)

{
  long lVar1;
  
  lVar1 = FUN_ffff0000be05f1bc(0xb8);
  if (lVar1 != 0) {
    FUN_ffff0000be060ae0(lVar1,0,0xb8);
    *(undefined4 *)(lVar1 + 0xa8) = param_2;
    *(undefined8 *)(lVar1 + 0xb0) = param_4;
    FUN_ffff0000be05e174(lVar1,param_1,0x1000,param_3,0,0,4);
    *(code **)(lVar1 + 0x78) = FUN_ffff0000be072588;
    *(code **)(lVar1 + 0x88) = FUN_ffff0000be072630;
    *(code **)(lVar1 + 0x90) = FUN_ffff0000be0726d8;
    *(code **)(lVar1 + 0x98) = FUN_ffff0000be07285c;
    FUN_ffff0000be05e934(lVar1);
    FUN_ffff0000be0624a4(*(undefined8 *)(lVar1 + 0x18),0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_common_ufs_ufs_ffff0000be0b5d0f,0x138,&DAT_ffff0000be0bdb6e);
}


