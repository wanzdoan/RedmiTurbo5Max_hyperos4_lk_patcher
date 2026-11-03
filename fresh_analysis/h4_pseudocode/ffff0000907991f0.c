
ulong FUN_ffff0000907991f0(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  undefined *puVar1;
  int iVar2;
  long lVar3;
  ulong uVar4;
  
  FUN_ffff00009077e074
            (DAT_ffff000090839a28 + 0x10,s_LTC_ARGCHK___s__failure_on_line___ffff0000907fac08,
             param_1,param_3,param_2);
  lVar3 = FUN_ffff00009077d714();
  puVar1 = PTR_DAT_ffff000090839a90;
  if (lVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000907991f0
              (s_name____NULL_ffff0000907d0618,s_platform_mediatek_mt6991_sboot_c_ffff0000907fe563,
               0x19);
  }
  uVar4 = 0;
  do {
    if ((*(undefined8 **)(puVar1 + uVar4 * 8) != (undefined8 *)0x0) &&
       (iVar2 = FUN_ffff00009077f0ac(**(undefined8 **)(puVar1 + uVar4 * 8),lVar3), iVar2 == 0))
    goto LAB_ffff000090799280;
    uVar4 = uVar4 + 1;
  } while (uVar4 != 0x22);
  uVar4 = 0xffffffff;
LAB_ffff000090799280:
  return uVar4 & 0xffffffff;
}


