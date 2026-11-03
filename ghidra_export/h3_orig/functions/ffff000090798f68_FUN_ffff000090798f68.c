// ffff000090798f68  size=136  FUN_ffff000090798f68

ulong FUN_ffff000090798f68(long param_1)

{
  long lVar1;
  int iVar2;
  undefined8 *puVar3;
  ulong uVar4;
  
  lVar1 = DAT_ffff000090839a70;
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff000090798f30
              (s_name____NULL_ffff0000907d05f9,s_platform_mediatek_mt6991_sboot_c_ffff0000907fe3bb,
               0x19);
  }
  uVar4 = 0;
  do {
    puVar3 = *(undefined8 **)(lVar1 + uVar4 * 8);
    if ((puVar3 != (undefined8 *)0x0) && (iVar2 = FUN_ffff00009077ede4(*puVar3,param_1), iVar2 == 0)
       ) goto LAB_ffff000090798fc0;
    uVar4 = uVar4 + 1;
  } while (uVar4 != 0x22);
  uVar4 = 0xffffffff;
LAB_ffff000090798fc0:
  return uVar4 & 0xffffffff;
}


