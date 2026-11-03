// ffff000090798f30  size=56  FUN_ffff000090798f30

ulong FUN_ffff000090798f30(undefined8 param_1,undefined8 param_2,undefined4 param_3)

{
  long lVar1;
  int iVar2;
  long lVar3;
  undefined8 *puVar4;
  ulong uVar5;
  
  FUN_ffff00009077ddac
            (DAT_ffff000090839a08 + 0x10,s_LTC_ARGCHK___s__failure_on_line___ffff0000907faa6c,
             param_1,param_3,param_2);
  lVar3 = FUN_ffff00009077d44c();
  lVar1 = DAT_ffff000090839a70;
  if (lVar3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_name____NULL_ffff0000907d05f9,s_platform_mediatek_mt6991_sboot_c_ffff0000907fe3bb,
               0x19);
  }
  uVar5 = 0;
  do {
    puVar4 = *(undefined8 **)(lVar1 + uVar5 * 8);
    if ((puVar4 != (undefined8 *)0x0) && (iVar2 = FUN_ffff00009077ede4(*puVar4,lVar3), iVar2 == 0))
    goto LAB_ffff000090798fc0;
    uVar5 = uVar5 + 1;
  } while (uVar5 != 0x22);
  uVar5 = 0xffffffff;
LAB_ffff000090798fc0:
  return uVar5 & 0xffffffff;
}


