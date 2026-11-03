
void FUN_ffff0000b8027a68(void)

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
    puVar1 = &DAT_ffff0000b811cf40 + lVar10;
    (&DAT_ffff0000b811cf38)[lVar11] = 0;
    FUN_ffff0000b804d070(puVar1,0,0x4000);
    (&DAT_ffff0000b8124f48)[lVar11 * 4] = &DAT_ffff0000b811cf48 + lVar10;
    lVar5 = FUN_ffff0000b804a99c(&DAT_ffff0000b808aacf);
    if (lVar5 != 0) {
      lVar6 = FUN_ffff0000b804aa30(lVar5,puVar1,uVar12,0x4000);
      FUN_ffff0000b804a920(lVar5);
      if (lVar6 == 0x4000) {
        lVar5 = lVar11 * 0x20;
        FUN_ffff0000b804cd40(&DAT_ffff0000b8124f40 + lVar5,puVar1,8);
        FUN_ffff0000b804cd40(&DAT_ffff0000b8124f50 + lVar5,&DAT_ffff0000b8120f34 + lVar10,8);
        (&DAT_ffff0000b8124f47)[lVar5] = 0;
        (&DAT_ffff0000b8124f57)[lVar5] = 0;
        iVar4 = FUN_ffff0000b804d248(&DAT_ffff0000b8124f40 + lVar5,s_ENV_v1_ffff0000b8087ed3);
        if ((iVar4 == 0) &&
           (iVar4 = FUN_ffff0000b804d248(&DAT_ffff0000b8124f50 + lVar5,s_ENV_v1_ffff0000b8087ed3),
           iVar4 == 0)) {
          lVar5 = (&DAT_ffff0000b8124f48)[lVar11 * 4];
          iVar4 = 0;
          iVar8 = 0;
          iVar2 = (&DAT_ffff0000b8120f3c)[lVar11 * 0x1000];
          lVar10 = 0x3fec;
          (&DAT_ffff0000b8124f58)[lVar11 * 8] = iVar2;
          pbVar9 = (byte *)(lVar5 + 1);
          do {
            lVar10 = lVar10 + -2;
            iVar4 = iVar4 + (uint)pbVar9[-1];
            iVar8 = iVar8 + (uint)*pbVar9;
            pbVar9 = pbVar9 + 2;
          } while (lVar10 != 0);
          if (iVar8 + iVar4 == iVar2) {
            FUN_ffff0000b804c33c(s_ENV_of_area__d_initialize_succes_ffff0000b808fd20,lVar11);
            (&DAT_ffff0000b811cf38)[lVar11] = 1;
            goto LAB_ffff0000b8027ad0;
          }
          FUN_ffff0000b804c33c(s_checksum_of_area__d_mismatch_s___ffff0000b809f895,lVar11);
          iVar4 = (&DAT_ffff0000b811cf38)[lVar11];
        }
        else {
          FUN_ffff0000b804c33c(s_ENV_SIG_of_area__d_not_found__it_ffff0000b80926e8,lVar11);
          iVar4 = (&DAT_ffff0000b811cf38)[lVar11];
        }
        if (iVar4 == 0) {
          FUN_ffff0000b804d070(puVar1,0,0x4000);
        }
      }
    }
LAB_ffff0000b8027ad0:
    uVar12 = 0x24000;
    lVar11 = 1;
    bVar3 = !bVar7;
    bVar7 = false;
    if (bVar3) {
      return;
    }
  } while( true );
}


