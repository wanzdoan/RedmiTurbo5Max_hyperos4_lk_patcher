
ulong FUN_ffff0000b805b700(long param_1)

{
  undefined *puVar1;
  int iVar2;
  ulong uVar3;
  
  puVar1 = PTR_DAT_ffff0000b81087e0;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_name____NULL_ffff0000b8085ce0,s_platform_mediatek_mt6991_sboot_c_ffff0000b809efef,
               0x19);
  }
  uVar3 = 0;
  do {
    if ((*(undefined8 **)(puVar1 + uVar3 * 8) != (undefined8 *)0x0) &&
       (iVar2 = FUN_ffff0000b804d248(**(undefined8 **)(puVar1 + uVar3 * 8),param_1), iVar2 == 0))
    goto LAB_ffff0000b805b758;
    uVar3 = uVar3 + 1;
  } while (uVar3 != 0x22);
  uVar3 = 0xffffffff;
LAB_ffff0000b805b758:
  return uVar3 & 0xffffffff;
}


