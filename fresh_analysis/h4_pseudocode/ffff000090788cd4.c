
undefined * FUN_ffff000090788cd4(undefined8 param_1)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  long lVar6;
  ulong uVar7;
  
  lVar3 = FUN_ffff00009077d55c(0x40,0x300d);
  if (lVar3 == 0) {
    FUN_ffff00009077e11c(s_Memory_alloc_failed__ffff0000907e871f);
  }
  else {
    lVar6 = 0x800;
    uVar7 = 0x3800;
    do {
      lVar4 = FUN_ffff00009077c028(param_1);
      if ((lVar4 == 0) && (lVar4 = FUN_ffff00009077bf18(param_1), lVar4 == 0)) {
        FUN_ffff00009077e11c(s_failed_to_open__s_ffff0000907d51ec,param_1);
        iVar2 = -0x16;
LAB_ffff000090788e24:
        pcVar5 = s_Read_preloader_failed__ret___0x__ffff0000907ea137;
        goto LAB_ffff000090788e30;
      }
      iVar2 = FUN_ffff00009077c0bc(lVar4,lVar3,lVar6,0x300d);
      FUN_ffff00009077bfac(lVar4);
      if (iVar2 < 0) goto LAB_ffff000090788e24;
      lVar4 = 0;
      lVar6 = lVar6 + 0x3000;
      do {
        iVar2 = FUN_ffff00009077eae8(lVar3 + lVar4,s_AND_ROMINFO_v_ffff0000907ea107,0xd);
        if (iVar2 == 0) {
          FUN_ffff00009077ee50(&DAT_ffff00009088736c,0,0x24);
          FUN_ffff00009077eb20(&DAT_ffff00009088736c,lVar3 + lVar4,0x24);
          thunk_FUN_ffff0000907c314c(lVar3);
          return &DAT_ffff000090887380;
        }
        lVar4 = lVar4 + 1;
      } while (lVar4 < 0x3000);
      bVar1 = uVar7 < 0x9d000;
      uVar7 = uVar7 + 0x3000;
    } while (bVar1);
    pcVar5 = s_Find_no_rom_info_id__ret___0x_x_ffff000090801874;
    iVar2 = 0x10a007;
LAB_ffff000090788e30:
    FUN_ffff00009077e11c(pcVar5,iVar2);
    thunk_FUN_ffff0000907c314c(lVar3);
  }
  return (undefined *)0x0;
}


