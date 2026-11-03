
void FUN_ffff0000be031a60(void)

{
  undefined *puVar1;
  int iVar2;
  bool bVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  bool bVar7;
  int iVar8;
  byte *pbVar9;
  long lVar10;
  long lVar11;
  undefined8 uVar12;
  
  lVar11 = 0;
  uVar12 = 0x20000;
  bVar7 = true;
  do {
    lVar10 = lVar11 * 0x4000;
    puVar1 = &DAT_ffff0000be119730 + lVar10;
    (&DAT_ffff0000be119728)[lVar11] = 0;
    FUN_ffff0000be060ae0(puVar1,0,0x4000);
    (&DAT_ffff0000be121738)[lVar11 * 4] = &DAT_ffff0000be119738 + lVar10;
    lVar5 = FUN_ffff0000be05df6c(&DAT_ffff0000be0aa4f5);
    if (lVar5 != 0) {
      lVar6 = FUN_ffff0000be05e000(lVar5,puVar1,uVar12,0x4000);
      FUN_ffff0000be05def0(lVar5);
      if (lVar6 == 0x4000) {
        lVar5 = lVar11 * 0x20;
        FUN_ffff0000be0607b0(&DAT_ffff0000be121730 + lVar5,puVar1,8);
        FUN_ffff0000be0607b0(&DAT_ffff0000be121740 + lVar5,&DAT_ffff0000be11d724 + lVar10,8);
        (&DAT_ffff0000be121737)[lVar5] = 0;
        (&DAT_ffff0000be121747)[lVar5] = 0;
        iVar4 = FUN_ffff0000be060cb8(&DAT_ffff0000be121730 + lVar5,s_ENV_v1_ffff0000be0a6bc5);
        if ((iVar4 == 0) &&
           (iVar4 = FUN_ffff0000be060cb8(&DAT_ffff0000be121740 + lVar5,s_ENV_v1_ffff0000be0a6bc5),
           iVar4 == 0)) {
          lVar5 = (&DAT_ffff0000be121738)[lVar11 * 4];
          iVar4 = 0;
          iVar8 = 0;
          iVar2 = (&DAT_ffff0000be11d72c)[lVar11 * 0x1000];
          lVar10 = 0x3fec;
          (&DAT_ffff0000be121748)[lVar11 * 8] = iVar2;
          pbVar9 = (byte *)(lVar5 + 1);
          do {
            lVar10 = lVar10 + -2;
            iVar4 = iVar4 + (uint)pbVar9[-1];
            iVar8 = iVar8 + (uint)*pbVar9;
            pbVar9 = pbVar9 + 2;
          } while (lVar10 != 0);
          if (iVar8 + iVar4 == iVar2) {
            FUN_ffff0000be05fd98(s_ENV_of_area__d_initialize_succes_ffff0000be0b0dc0,lVar11);
            (&DAT_ffff0000be119728)[lVar11] = 1;
            goto LAB_ffff0000be031ac8;
          }
          FUN_ffff0000be05fd98(s_checksum_of_area__d_mismatch_s___ffff0000be0c41e0,lVar11);
          iVar4 = (&DAT_ffff0000be119728)[lVar11];
        }
        else {
          FUN_ffff0000be05fd98(s_ENV_SIG_of_area__d_not_found__it_ffff0000be0b41a2,lVar11);
          iVar4 = (&DAT_ffff0000be119728)[lVar11];
        }
        if (iVar4 == 0) {
          FUN_ffff0000be060ae0(puVar1,0,0x4000);
        }
      }
    }
LAB_ffff0000be031ac8:
    uVar12 = 0x24000;
    lVar11 = 1;
    bVar3 = !bVar7;
    bVar7 = false;
    if (bVar3) {
      return;
    }
  } while( true );
}


