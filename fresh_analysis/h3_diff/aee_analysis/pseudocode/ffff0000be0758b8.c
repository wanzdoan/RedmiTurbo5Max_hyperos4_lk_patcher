
ulong FUN_ffff0000be0758b8(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  undefined *puVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  
  FUN_ffff0000be05fcf0
            (PTR_PTR_ffff0000be0f56b8 + 0x10,s_LTC_ARGCHK___s__failure_on_line___ffff0000be0c147c,
             param_1,param_3,param_2);
  lVar3 = FUN_ffff0000be05f3bc();
  puVar1 = PTR_DAT_ffff0000be0f5710;
  if (lVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be0758b8
              (s_name____NULL_ffff0000be0a3ed9,s_platform_mediatek_mt6991_sboot_c_ffff0000be0c386f,
               0x19);
  }
  uVar4 = 0;
  do {
    if ((*(undefined8 **)(puVar1 + uVar4 * 8) != (undefined8 *)0x0) &&
       (iVar2 = FUN_ffff0000be060cb8(**(undefined8 **)(puVar1 + uVar4 * 8),lVar3), iVar2 == 0))
    goto LAB_ffff0000be075948;
    uVar4 = uVar4 + 1;
  } while (uVar4 != 0x22);
  uVar4 = 0xffffffff;
LAB_ffff0000be075948:
  return uVar4 & 0xffffffff;
}


