
int FUN_ffff0000b8056970(long param_1)

{
  char *pcVar1;
  byte bVar2;
  char cVar3;
  uint uVar4;
  int iVar5;
  undefined1 *puVar6;
  ulong uVar7;
  char cVar8;
  ulong uVar9;
  uint uVar10;
  undefined1 *puVar11;
  long lVar12;
  
  puVar6 = (undefined1 *)FUN_ffff0000b804bd14(0x40,0x100);
  if (puVar6 == (undefined1 *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0xb8c,
               s_ufs_temp_buf_ffff0000b8085b07);
  }
  iVar5 = (**(code **)(param_1 + 0x228))
                    (param_1,1,0,*(undefined1 *)(param_1 + 0x1a8),puVar6,DAT_ffff0000b81063ec);
  if (iVar5 == 0) {
    FUN_ffff0000b804c33c
              (s__UFS___s__original_Configuration_ffff0000b8089648,
               s_ufs_check_lu_cfg_ffff0000b809dede);
    uVar4 = DAT_ffff0000b81063ec;
    uVar9 = (ulong)DAT_ffff0000b81063ec;
    bVar2 = *(byte *)(param_1 + 0x128);
    if ((ulong)*(byte *)(param_1 + 0x127) == 0) {
      uVar10 = 0;
    }
    else {
      uVar7 = (ulong)*(byte *)(param_1 + 0x127) - 1;
      uVar10 = DAT_ffff0000b81063ec;
      if (DAT_ffff0000b81063ec < 2) {
        uVar10 = 1;
      }
      if (uVar10 - 1 <= uVar7) {
        uVar7 = (ulong)(uVar10 - 1);
      }
      lVar12 = uVar7 + 1;
      puVar11 = puVar6;
      do {
        FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,*puVar11);
        lVar12 = lVar12 + -1;
        puVar11 = puVar11 + 1;
      } while (lVar12 != 0);
      uVar10 = (int)uVar7 + 1;
    }
    FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    do {
      if (uVar4 <= uVar10) break;
      if (bVar2 != 0) {
        lVar12 = 0;
        do {
          FUN_ffff0000b804c33c(&DAT_ffff0000b808875a,puVar6[lVar12 + (ulong)uVar10]);
          lVar12 = lVar12 + 1;
          if (uVar9 <= lVar12 + (ulong)uVar10) break;
        } while ((uint)lVar12 < (uint)bVar2);
        uVar10 = uVar10 + (uint)lVar12;
      }
      FUN_ffff0000b804c33c(&DAT_ffff0000b809b862);
    } while (uVar10 < uVar4);
    pcVar1 = puVar6 + *(byte *)(param_1 + 0x127);
    if ((*pcVar1 == '\x01') ||
       ((cVar8 = '\0', *(char *)(param_1 + 0x129) != '\0' && (*pcVar1 == '\x02')))) {
      cVar8 = '\x01';
    }
    pcVar1 = pcVar1 + *(byte *)(param_1 + 0x128);
    if ((*pcVar1 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (*pcVar1 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    pcVar1 = pcVar1 + *(byte *)(param_1 + 0x128);
    cVar3 = *pcVar1;
    if ((cVar3 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (cVar3 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    if ((*(char *)(param_1 + 0x129) != '\0') && (cVar3 != '\x02')) {
      FUN_ffff0000b804c33c(s__UFS__HPB_is_supported_but_not_e_ffff0000b808e5be,2);
    }
    bVar2 = *(byte *)(param_1 + 0x128);
    pcVar1 = pcVar1 + bVar2;
    if ((*pcVar1 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (*pcVar1 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    pcVar1 = pcVar1 + bVar2;
    if ((*pcVar1 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (*pcVar1 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    pcVar1 = pcVar1 + bVar2;
    if ((*pcVar1 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (*pcVar1 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    cVar3 = pcVar1[bVar2];
    if ((cVar3 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (cVar3 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    cVar3 = (pcVar1 + bVar2)[bVar2];
    if ((cVar3 == '\x01') || ((*(char *)(param_1 + 0x129) != '\0' && (cVar3 == '\x02')))) {
      cVar8 = cVar8 + '\x01';
    }
    iVar5 = 0;
    *(char *)(param_1 + 0x8a) = cVar8;
  }
  else {
    FUN_ffff0000b804c33c
              (s__UFS__err___s__read_config_descr_ffff0000b809bef1,
               s_ufs_check_lu_cfg_ffff0000b809dede,iVar5);
  }
  thunk_FUN_ffff0000b8082558(puVar6);
  return iVar5;
}


