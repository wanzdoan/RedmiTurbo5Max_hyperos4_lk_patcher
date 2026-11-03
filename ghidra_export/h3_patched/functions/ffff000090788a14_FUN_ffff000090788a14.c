// ffff000090788a14  size=364  FUN_ffff000090788a14

undefined8 FUN_ffff000090788a14(undefined8 param_1)

{
  bool bVar1;
  int iVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  long lVar6;
  ulong uVar7;
  
  lVar3 = FUN_ffff00009077d294(0x40,0x300d);
  if (lVar3 == 0) {
    FUN_ffff00009077de54(s_Memory_alloc_failed__ffff0000907e85c4);
  }
  else {
    lVar6 = 0x800;
    uVar7 = 0x3800;
    do {
      lVar4 = FUN_ffff00009077bd60(param_1);
      if ((lVar4 == 0) && (lVar4 = FUN_ffff00009077bc50(param_1), lVar4 == 0)) {
        FUN_ffff00009077de54(s_failed_to_open__s_ffff0000907d51af,param_1);
        iVar2 = -0x16;
LAB_ffff000090788b64:
        pcVar5 = s_Read_preloader_failed__ret___0x__ffff0000907e9fdc;
        goto LAB_ffff000090788b70;
      }
      iVar2 = FUN_ffff00009077bdf4(lVar4,lVar3,lVar6,0x300d);
      FUN_ffff00009077bce4(lVar4);
      if (iVar2 < 0) goto LAB_ffff000090788b64;
      lVar4 = 0;
      lVar6 = lVar6 + 0x3000;
      do {
        iVar2 = FUN_ffff00009077e820(lVar3 + lVar4,s_AND_ROMINFO_v_ffff0000907e9fac,0xd);
        if (iVar2 == 0) {
          FUN_ffff00009077eb88(0xffff0000908872d4,0,0x24);
          FUN_ffff00009077e858(0xffff0000908872d4,lVar3 + lVar4,0x24);
          thunk_FUN_ffff0000907c2ad8(lVar3);
          return 0xffff0000908872e8;
        }
        lVar4 = lVar4 + 1;
      } while (lVar4 < 0x3000);
      bVar1 = uVar7 < 0x9d000;
      uVar7 = uVar7 + 0x3000;
    } while (bVar1);
    pcVar5 = s_Find_no_rom_info_id__ret___0x_x_ffff000090801698;
    iVar2 = 0x10a007;
LAB_ffff000090788b70:
    FUN_ffff00009077de54(pcVar5,iVar2);
    thunk_FUN_ffff0000907c2ad8(lVar3);
  }
  return 0;
}


