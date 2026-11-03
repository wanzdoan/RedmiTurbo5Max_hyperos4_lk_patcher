// File offset entry: 0x6e584

int FUN_ffff0000b806e584(long param_1,int param_2,char *param_3)

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
  
  FUN_ffff0000b804cbe4(&DAT_ffff0000b81b8118,0,0x408);
  DAT_ffff0000b81b8318 = 0x200;
  DAT_ffff0000b81b851c = 0x200;
  DAT_ffff0000b81b7e60 = param_1 + (ulong)*(uint *)(param_1 + 0x34);
  uVar10 = param_2 - *(uint *)(param_1 + 0x34);
  DAT_ffff0000b81b7e70 = (ulong)uVar10;
  DAT_ffff0000b81b8108 = DAT_ffff0000b81b7e60;
  DAT_ffff0000b81b8110 = DAT_ffff0000b81b7e70;
  if (uVar10 == 0) {
LAB_ffff0000b806e624:
    iVar4 = 0x7014;
    DAT_ffff0000b81b8108 = 0;
    DAT_ffff0000b81b8110 = 0;
LAB_ffff0000b806e630:
    FUN_ffff0000b806cf8c(param_1,param_2,0x1f4e2);
  }
  else {
    do {
      iVar4 = FUN_ffff0000b806eac8(&DAT_ffff0000b81b8118);
      if (iVar4 == 0) break;
    } while (DAT_ffff0000b81b8110 != 0);
    if (iVar4 != 0) goto LAB_ffff0000b806e624;
    uVar10 = *(uint *)(param_3 + 0x200);
    uVar18 = (ulong)uVar10;
    DAT_ffff0000b81b8108 = 0;
    DAT_ffff0000b81b8110 = 0;
    if (uVar10 != DAT_ffff0000b81b8318) {
      iVar4 = 0x7017;
      goto LAB_ffff0000b806e630;
    }
    uVar16 = *(uint *)(param_3 + 0x404);
    uVar9 = (ulong)uVar16;
    if (uVar16 != DAT_ffff0000b81b851c) {
      iVar4 = 0x7015;
      goto LAB_ffff0000b806e630;
    }
    if (uVar10 != 0) {
      pcVar14 = (char *)&DAT_ffff0000b81b8118;
      pcVar12 = param_3;
      uVar13 = uVar18;
      do {
        if (*pcVar12 != *pcVar14) {
          iVar4 = 0x7018;
          goto LAB_ffff0000b806e630;
        }
        uVar13 = uVar13 - 1;
        pcVar14 = pcVar14 + 1;
        pcVar12 = pcVar12 + 1;
      } while (uVar13 != 0);
    }
    if (uVar16 != 0) {
      param_3 = param_3 + 0x204;
      pcVar14 = (char *)&DAT_ffff0000b81b831c;
      do {
        if (*param_3 != *pcVar14) {
          iVar4 = 0x7016;
          goto LAB_ffff0000b806e630;
        }
        uVar9 = uVar9 - 1;
        pcVar14 = pcVar14 + 1;
        param_3 = param_3 + 1;
      } while (uVar9 != 0);
    }
    iVar4 = FUN_ffff0000b80613f8(uVar18);
    if (iVar4 == 0) {
      iVar4 = 0x101026;
      goto LAB_ffff0000b806e630;
    }
    FUN_ffff0000b804cbe4(&DAT_ffff0000b81b7e80,0,0x200);
    FUN_ffff0000b80695a0(&DAT_ffff0000b81b7e80,&DAT_ffff0000b81b8118,uVar18);
    DAT_ffff0000b81b8080 = uVar10;
    if (1 < uVar10) {
      uVar18 = (ulong)(uVar10 >> 1);
      puVar15 = &DAT_ffff0000b81b7e80;
      do {
        uVar10 = uVar10 - 1;
        uVar18 = uVar18 - 1;
        uVar1 = *puVar15;
        *puVar15 = (&DAT_ffff0000b81b7e80)[uVar10];
        (&DAT_ffff0000b81b7e80)[uVar10] = uVar1;
        puVar15 = puVar15 + 1;
      } while (uVar18 != 0);
    }
    iVar4 = FUN_ffff0000b806dfa0();
    if (iVar4 != 0) goto LAB_ffff0000b806e630;
    DAT_ffff0000b81b7e70 = (ulong)DAT_ffff0000b81b7e78;
    DAT_ffff0000b81b7e60 = DAT_ffff0000b81b7e68;
    iVar4 = FUN_ffff0000b806cf8c(param_1,param_2,0x1f4e2);
  }
  puVar3 = PTR_DAT_ffff0000b8107730;
  iVar5 = FUN_ffff0000b806b64c(PTR_DAT_ffff0000b8107730 + 0x80,0x40,PTR_DAT_ffff0000b8107730 + 0xc0)
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
  lVar7 = FUN_ffff0000b8069dc8();
  if (lVar7 == 0) {
    iVar6 = FUN_ffff0000b8069b68();
    iVar5 = 0x100006;
    if (iVar6 != 0) {
      FUN_ffff0000b804beb0
                (s___s__Chip_SOCID_shouldn_t_be_NUL_ffff0000b809a120,&DAT_ffff0000b809770d);
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
      iVar5 = FUN_ffff0000b804d038(lVar7,local_70,0x20);
      if (iVar5 != 0) {
        iVar5 = FUN_ffff0000b8069b68();
        if (iVar5 != 0) {
          FUN_ffff0000b804beb0(s_Socid_signed_in_image_not_match_w_ffff0000b809af32);
        }
        iVar6 = FUN_ffff0000b8069b68();
        iVar5 = 0x101022;
        if (iVar6 != 0) {
          FUN_ffff0000b804beb0(s_Image_is_signed_for_special_devi_ffff0000b80894cf);
        }
        goto LAB_ffff0000b806e8e4;
      }
    }
    else {
      iVar5 = FUN_ffff0000b8069b68();
      if (iVar5 != 0) {
        FUN_ffff0000b804beb0
                  (s___s__Cert_socid_size_wrong__may_b_ffff0000b8086b35,&DAT_ffff0000b809770d);
      }
    }
    iVar5 = 0;
  }
LAB_ffff0000b806e8e4:
  if (iVar4 != 0) {
    iVar5 = iVar4;
  }
  return iVar5;
}


