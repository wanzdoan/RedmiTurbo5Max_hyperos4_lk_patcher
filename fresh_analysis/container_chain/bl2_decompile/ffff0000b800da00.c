
undefined8 FUN_ffff0000b800da00(void)

{
  undefined8 uVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  undefined8 *puVar6;
  long lVar7;
  
  FUN_ffff0000b800ad90(&LAB_ffff0000b80adcf8,0xffffffff);
  if (DAT_ffff0000b8116158 == 0) {
    lVar7 = 0;
  }
  else {
    lVar7 = 0;
    iVar3 = 0;
    iVar5 = DAT_ffff0000b8116158;
    iVar4 = DAT_ffff0000b8116158;
    do {
      if (iVar4 != 0) {
        puVar6 = (undefined8 *)(DAT_ffff0000b8115508 + 0xc18);
        do {
          iVar5 = iVar3;
          uVar1 = *puVar6;
          lVar2 = puVar6[1];
          FUN_ffff0000b804c33c
                    (s_Free_mblock_R__d__start__0x_llx__ffff0000b809d3e2,iVar5,uVar1,lVar2,
                     *(undefined4 *)(puVar6 + 2),(long)puVar6 + 0x14);
          FUN_ffff0000b800d360(uVar1,lVar2);
          lVar7 = lVar2 + lVar7;
          if (DAT_ffff0000b8116158 == 0) break;
          puVar6 = puVar6 + 0x13;
          iVar3 = iVar5 + 1;
        } while (puVar6 <= (undefined8 *)(DAT_ffff0000b8115508 + 0x5780U));
        iVar3 = iVar5 + 1;
        iVar5 = DAT_ffff0000b8116158;
        iVar4 = DAT_ffff0000b8116158;
      }
    } while (iVar5 != 0);
  }
  FUN_ffff0000b800ae28(&LAB_ffff0000b80adcf8);
  FUN_ffff0000b804c33c(s_Free_Total_DRAM___0x_llx_ffff0000b8096e9a,lVar7);
  DAT_ffff0000b8115518 = 1;
  return 0;
}


