
int FUN_ffff0000907b25b4(long param_1,int param_2,char *param_3)

{
  undefined1 uVar1;
  byte bVar2;
  undefined *puVar3;
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
  
  FUN_ffff00009077ee50(&DAT_ffff00009090cf58,0,0x408);
  DAT_ffff00009090d158 = 0x200;
  DAT_ffff00009090d35c = 0x200;
  DAT_ffff00009090cc68 = param_1 + (ulong)*(uint *)(param_1 + 0x34);
  uVar10 = param_2 - *(uint *)(param_1 + 0x34);
  DAT_ffff00009090cc78 = (ulong)uVar10;
  DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
  DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
  if (uVar10 == 0) {
LAB_ffff0000907b2654:
    iVar4 = 0x7014;
    DAT_ffff00009090cf48 = 0;
    DAT_ffff00009090cf50 = 0;
LAB_ffff0000907b2660:
    FUN_ffff0000907b0ec4(param_1,param_2,0x1f4e2);
  }
  else {
    do {
      iVar4 = FUN_ffff0000907b2b0c(&DAT_ffff00009090cf58);
      if (iVar4 == 0) break;
    } while (DAT_ffff00009090cf50 != 0);
    if (iVar4 != 0) goto LAB_ffff0000907b2654;
    uVar10 = *(uint *)(param_3 + 0x200);
    uVar18 = (ulong)uVar10;
    DAT_ffff00009090cf48 = 0;
    DAT_ffff00009090cf50 = 0;
    if (uVar10 != DAT_ffff00009090d158) {
      iVar4 = 0x7017;
      goto LAB_ffff0000907b2660;
    }
    uVar16 = *(uint *)(param_3 + 0x404);
    uVar9 = (ulong)uVar16;
    if (uVar16 != DAT_ffff00009090d35c) {
      iVar4 = 0x7015;
      goto LAB_ffff0000907b2660;
    }
    if (uVar10 != 0) {
      pcVar14 = (char *)&DAT_ffff00009090cf58;
      pcVar12 = param_3;
      uVar13 = uVar18;
      do {
        if (*pcVar12 != *pcVar14) {
          iVar4 = 0x7018;
          goto LAB_ffff0000907b2660;
        }
        uVar13 = uVar13 - 1;
        pcVar14 = pcVar14 + 1;
        pcVar12 = pcVar12 + 1;
      } while (uVar13 != 0);
    }
    if (uVar16 != 0) {
      param_3 = param_3 + 0x204;
      pcVar14 = (char *)&DAT_ffff00009090d15c;
      do {
        if (*param_3 != *pcVar14) {
          iVar4 = 0x7016;
          goto LAB_ffff0000907b2660;
        }
        uVar9 = uVar9 - 1;
        pcVar14 = pcVar14 + 1;
        param_3 = param_3 + 1;
      } while (uVar9 != 0);
    }
    iVar4 = FUN_ffff0000907a2b7c(uVar18);
    if (iVar4 == 0) {
      iVar4 = 0x101026;
      goto LAB_ffff0000907b2660;
    }
    FUN_ffff00009077ee50(&DAT_ffff00009090ccc0,0,0x200);
    FUN_ffff0000907ad234(&DAT_ffff00009090ccc0,&DAT_ffff00009090cf58,uVar18);
    DAT_ffff00009090cec0 = uVar10;
    if (1 < uVar10) {
      uVar18 = (ulong)(uVar10 >> 1);
      puVar15 = &DAT_ffff00009090ccc0;
      do {
        uVar10 = uVar10 - 1;
        uVar18 = uVar18 - 1;
        uVar1 = *puVar15;
        *puVar15 = (&DAT_ffff00009090ccc0)[uVar10];
        (&DAT_ffff00009090ccc0)[uVar10] = uVar1;
        puVar15 = puVar15 + 1;
      } while (uVar18 != 0);
    }
    DAT_ffff00009090cc68 = param_1 + (ulong)*(uint *)(param_1 + 0x34);
    DAT_ffff00009090cc78 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
    iVar4 = FUN_ffff0000907b216c();
    if (iVar4 != 0) goto LAB_ffff0000907b2660;
    DAT_ffff00009090cc78 = (ulong)DAT_ffff00009090cc80;
    DAT_ffff00009090cc68 = DAT_ffff00009090cc70;
    iVar4 = FUN_ffff0000907b0ec4(param_1,param_2,0x1f4e2);
  }
  puVar3 = PTR_DAT_ffff000090839ab0;
  iVar5 = FUN_ffff0000907af2e8(PTR_DAT_ffff000090839ab0 + 0x80,0x40,PTR_DAT_ffff000090839ab0 + 0xc0)
  ;
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
  lVar7 = FUN_ffff0000907ada5c();
  if (lVar7 == 0) {
    iVar6 = FUN_ffff0000907ad7fc();
    iVar5 = 0x100006;
    if (iVar6 != 0) {
      FUN_ffff00009077e11c
                (s___s__Chip_SOCID_shouldn_t_be_NUL_ffff0000907f75fc,&DAT_ffff0000907f228a);
      iVar5 = 0x100006;
    }
  }
  else {
    if (*(int *)(puVar3 + 0xc0) == 0x40) {
      lVar8 = 0;
      pbVar11 = puVar3 + 0x81;
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
      iVar5 = FUN_ffff00009077f348(lVar7,local_70,0x20);
      if (iVar5 != 0) {
        iVar5 = FUN_ffff0000907ad7fc();
        if (iVar5 != 0) {
          FUN_ffff00009077e11c(s_Socid_signed_in_image_not_match_w_ffff0000907f90cf);
        }
        iVar6 = FUN_ffff0000907ad7fc();
        iVar5 = 0x101022;
        if (iVar6 != 0) {
          FUN_ffff00009077e11c(s_Image_is_signed_for_special_devi_ffff0000907d8948);
        }
        goto LAB_ffff0000907b2928;
      }
    }
    else {
      iVar5 = FUN_ffff0000907ad7fc();
      if (iVar5 != 0) {
        FUN_ffff00009077e11c
                  (s___s__Cert_socid_size_wrong__may_b_ffff0000907d3b2e,&DAT_ffff0000907f228a);
      }
    }
    iVar5 = 0;
  }
LAB_ffff0000907b2928:
  if (iVar4 != 0) {
    iVar5 = iVar4;
  }
  return iVar5;
}


