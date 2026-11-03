// ffff0000907b1f58  size=956  FUN_ffff0000907b1f58

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907b1f58(long param_1,int param_2,char *param_3)

{
  undefined1 uVar1;
  byte bVar2;
  long lVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  uint uVar10;
  byte *pbVar11;
  char *pcVar12;
  ulong uVar13;
  char *pcVar14;
  undefined1 *puVar15;
  uint uVar16;
  uint uVar17;
  ulong uVar18;
  char local_70 [32];
  
  FUN_ffff00009077eb88(&DAT_ffff00009090ce98,0,0x408);
  _DAT_ffff00009090d098 = 0x200;
  _DAT_ffff00009090d29c = 0x200;
  _DAT_ffff00009090cba8 = param_1 + (ulong)*(uint *)(param_1 + 0x34);
  uVar10 = param_2 - *(uint *)(param_1 + 0x34);
  _DAT_ffff00009090cbb8 = (ulong)uVar10;
  _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
  _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
  if (uVar10 == 0) {
LAB_ffff0000907b1ff8:
    iVar4 = 0x7014;
    _DAT_ffff00009090ce88 = 0;
    _DAT_ffff00009090ce90 = 0;
LAB_ffff0000907b2004:
    FUN_ffff0000907b0960(param_1,param_2,0x1f4e2);
  }
  else {
    do {
      iVar4 = FUN_ffff0000907b249c(&DAT_ffff00009090ce98);
      if (iVar4 == 0) break;
    } while (_DAT_ffff00009090ce90 != 0);
    if (iVar4 != 0) goto LAB_ffff0000907b1ff8;
    uVar10 = *(uint *)(param_3 + 0x200);
    uVar18 = (ulong)uVar10;
    _DAT_ffff00009090ce88 = 0;
    _DAT_ffff00009090ce90 = 0;
    if (uVar10 != _DAT_ffff00009090d098) {
      iVar4 = 0x7017;
      goto LAB_ffff0000907b2004;
    }
    uVar16 = *(uint *)(param_3 + 0x404);
    uVar9 = (ulong)uVar16;
    if (uVar16 != _DAT_ffff00009090d29c) {
      iVar4 = 0x7015;
      goto LAB_ffff0000907b2004;
    }
    if (uVar10 != 0) {
      pcVar14 = &DAT_ffff00009090ce98;
      pcVar12 = param_3;
      uVar13 = uVar18;
      do {
        if (*pcVar12 != *pcVar14) {
          iVar4 = 0x7018;
          goto LAB_ffff0000907b2004;
        }
        uVar13 = uVar13 - 1;
        pcVar14 = pcVar14 + 1;
        pcVar12 = pcVar12 + 1;
      } while (uVar13 != 0);
    }
    if (uVar16 != 0) {
      param_3 = param_3 + 0x204;
      pcVar14 = &DAT_ffff00009090d09c;
      do {
        if (*param_3 != *pcVar14) {
          iVar4 = 0x7016;
          goto LAB_ffff0000907b2004;
        }
        uVar9 = uVar9 - 1;
        pcVar14 = pcVar14 + 1;
        param_3 = param_3 + 1;
      } while (uVar9 != 0);
    }
    iVar4 = FUN_ffff0000907a28bc(uVar18);
    if (iVar4 == 0) {
      iVar4 = 0x101026;
      goto LAB_ffff0000907b2004;
    }
    FUN_ffff00009077eb88(&DAT_ffff00009090cc00,0,0x200);
    FUN_ffff0000907acf74(&DAT_ffff00009090cc00,&DAT_ffff00009090ce98,uVar18);
    _DAT_ffff00009090ce00 = uVar10;
    if (1 < uVar10) {
      uVar18 = (ulong)(uVar10 >> 1);
      puVar15 = &DAT_ffff00009090cc00;
      do {
        uVar10 = uVar10 - 1;
        uVar18 = uVar18 - 1;
        uVar1 = *puVar15;
        *puVar15 = (&DAT_ffff00009090cc00)[uVar10];
        (&DAT_ffff00009090cc00)[uVar10] = uVar1;
        puVar15 = puVar15 + 1;
      } while (uVar18 != 0);
    }
    iVar4 = FUN_ffff0000907b1974();
    if (iVar4 != 0) goto LAB_ffff0000907b2004;
    _DAT_ffff00009090cbb8 = (ulong)_DAT_ffff00009090cbc0;
    _DAT_ffff00009090cba8 = _DAT_ffff00009090cbb0;
    iVar4 = FUN_ffff0000907b0960(param_1,param_2,0x1f4e2);
  }
  lVar3 = DAT_ffff000090839a90;
  iVar5 = FUN_ffff0000907af020(DAT_ffff000090839a90 + 0x80,0x40,DAT_ffff000090839a90 + 0xc0);
  if (iVar5 != 0) {
    return iVar4;
  }
  local_70[0x10] = '\0';
  local_70[0x11] = '\0';
  local_70[0x12] = '\0';
  local_70[0x13] = '\0';
  local_70[0x14] = '\0';
  local_70[0x15] = '\0';
  local_70[0x16] = '\0';
  local_70[0x17] = '\0';
  local_70[0x18] = '\0';
  local_70[0x19] = '\0';
  local_70[0x1a] = '\0';
  local_70[0x1b] = '\0';
  local_70[0x1c] = '\0';
  local_70[0x1d] = '\0';
  local_70[0x1e] = '\0';
  local_70[0x1f] = '\0';
  local_70[0] = '\0';
  local_70[1] = '\0';
  local_70[2] = '\0';
  local_70[3] = '\0';
  local_70[4] = '\0';
  local_70[5] = '\0';
  local_70[6] = '\0';
  local_70[7] = '\0';
  local_70[8] = '\0';
  local_70[9] = '\0';
  local_70[10] = '\0';
  local_70[0xb] = '\0';
  local_70[0xc] = '\0';
  local_70[0xd] = '\0';
  local_70[0xe] = '\0';
  local_70[0xf] = '\0';
  lVar7 = FUN_ffff0000907ad79c();
  if (lVar7 == 0) {
    iVar6 = FUN_ffff0000907ad53c();
    iVar5 = 0x100006;
    if (iVar6 != 0) {
      FUN_ffff00009077de54
                (s___s__Chip_SOCID_shouldn_t_be_NUL_ffff0000907f7472,&DAT_ffff0000907f20fe);
      iVar5 = 0x100006;
    }
  }
  else {
    if (*(int *)(lVar3 + 0xc0) == 0x40) {
      lVar8 = 0;
      pbVar11 = (byte *)(lVar3 + 0x81);
      do {
        bVar2 = pbVar11[-1];
        uVar10 = bVar2 - 0x30;
        if (9 < uVar10) {
          uVar16 = (uint)bVar2;
          uVar10 = uVar16 - 0x57;
          if (5 < bVar2 - 0x61) {
            uVar10 = uVar16;
          }
          if (uVar16 - 0x41 < 6) {
            uVar10 = uVar16 - 0x37;
          }
        }
        bVar2 = *pbVar11;
        uVar17 = (uint)bVar2;
        uVar16 = uVar17 - 0x30;
        if (9 < uVar17 - 0x30) {
          if (uVar17 - 0x41 < 6) {
            uVar16 = uVar17 - 0x37;
          }
          else {
            uVar16 = bVar2 - 0x57;
            if (5 < bVar2 - 0x61) {
              uVar16 = uVar17;
            }
          }
        }
        pbVar11 = pbVar11 + 2;
        local_70[lVar8] = (char)uVar16 + (char)uVar10 * '\x10';
        lVar8 = lVar8 + 1;
      } while (lVar8 != 0x20);
      iVar5 = FUN_ffff00009077f080(lVar7,local_70,0x20);
      if (iVar5 != 0) {
        iVar5 = FUN_ffff0000907ad53c();
        if (iVar5 != 0) {
          FUN_ffff00009077de54(s_Socid_signed_in_image_not_match_w_ffff0000907f8f33);
        }
        iVar6 = FUN_ffff0000907ad53c();
        iVar5 = 0x101022;
        if (iVar6 != 0) {
          FUN_ffff00009077de54(s_Image_is_signed_for_special_devi_ffff0000907d88c7);
        }
        goto LAB_ffff0000907b22b8;
      }
    }
    else {
      iVar5 = FUN_ffff0000907ad53c();
      if (iVar5 != 0) {
        FUN_ffff00009077de54
                  (s___s__Cert_socid_size_wrong__may_b_ffff0000907d3af1,&DAT_ffff0000907f20fe);
      }
    }
    iVar5 = 0;
  }
LAB_ffff0000907b22b8:
  if (iVar4 != 0) {
    iVar5 = iVar4;
  }
  return iVar5;
}


