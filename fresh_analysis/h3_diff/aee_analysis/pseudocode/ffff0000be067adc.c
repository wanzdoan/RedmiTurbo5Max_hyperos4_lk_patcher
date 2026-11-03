
undefined * FUN_ffff0000be067adc(undefined8 param_1)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  long lVar6;
  ulong uVar7;
  
  lVar3 = FUN_ffff0000be05f204(0x40,0x300d);
  if (lVar3 == 0) {
    FUN_ffff0000be05fd98(s_Memory_alloc_failed__ffff0000be0b4994);
  }
  else {
    lVar6 = 0x800;
    uVar7 = 0x3800;
    do {
      lVar4 = FUN_ffff0000be05df6c(param_1);
      if ((lVar4 == 0) && (lVar4 = FUN_ffff0000be05de5c(param_1), lVar4 == 0)) {
        FUN_ffff0000be05fd98(s_failed_to_open__s_ffff0000be0a76c8,param_1);
        iVar2 = -0x16;
LAB_ffff0000be067c2c:
        pcVar5 = s_Read_preloader_failed__ret___0x__ffff0000be0b5bf7;
        goto LAB_ffff0000be067c38;
      }
      iVar2 = FUN_ffff0000be05e000(lVar4,lVar3,lVar6,0x300d);
      FUN_ffff0000be05def0(lVar4);
      if (iVar2 < 0) goto LAB_ffff0000be067c2c;
      lVar4 = 0;
      lVar6 = lVar6 + 0x3000;
      do {
        iVar2 = FUN_ffff0000be060778(lVar3 + lVar4,s_AND_ROMINFO_v_ffff0000be0b5bc7,0xd);
        if (iVar2 == 0) {
          FUN_ffff0000be060ae0(&DAT_ffff0000be1323d0,0,0x24);
          FUN_ffff0000be0607b0(&DAT_ffff0000be1323d0,lVar3 + lVar4,0x24);
          thunk_FUN_ffff0000be09937c(lVar3);
          return &DAT_ffff0000be1323e4;
        }
        lVar4 = lVar4 + 1;
      } while (lVar4 < 0x3000);
      bVar1 = uVar7 < 0x9d000;
      uVar7 = uVar7 + 0x3000;
    } while (bVar1);
    pcVar5 = s_Find_no_rom_info_id__ret___0x_x_ffff0000be0c59c2;
    iVar2 = 0x10a007;
LAB_ffff0000be067c38:
    FUN_ffff0000be05fd98(pcVar5,iVar2);
    thunk_FUN_ffff0000be09937c(lVar3);
  }
  return (undefined *)0x0;
}


