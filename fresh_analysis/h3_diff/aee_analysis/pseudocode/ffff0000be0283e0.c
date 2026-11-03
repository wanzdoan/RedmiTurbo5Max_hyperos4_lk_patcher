
int FUN_ffff0000be0283e0(void)

{
  uint uVar1;
  short sVar2;
  bool bVar3;
  bool bVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  long lVar12;
  long lVar13;
  char *pcVar14;
  long lVar15;
  long lVar16;
  
  lVar6 = FUN_ffff0000be05de5c(s_bootdevice_ffff0000be0a45a8);
  if (lVar6 == 0) {
    return -0x13;
  }
  FUN_ffff0000be01a730(&LAB_ffff0000be0d6628,0xffffffff);
  if (DAT_ffff0000be0d6620 == 0) {
    DAT_ffff0000be0d6608 = FUN_ffff0000be05f204(0x40,*(undefined8 *)(lVar6 + 0x28));
    if (DAT_ffff0000be0d6608 == 0) {
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000be0b40a8;
    }
    else {
      iVar5 = FUN_ffff0000be027664(lVar6,DAT_ffff0000be0d6608,*(undefined8 *)(lVar6 + 0x28));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000be0bae59;
        FUN_ffff0000be05fd98(s__PART_UTILS__gpt_header_get_fail_ffff0000be0bae59);
        iVar5 = FUN_ffff0000be027664
                          (lVar6,DAT_ffff0000be0d6608,
                           *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
        if (iVar5 != 0) goto LAB_ffff0000be0285e0;
      }
      uVar1 = *(uint *)(DAT_ffff0000be0d6608 + 0x50);
      DAT_ffff0000be0d6610 = FUN_ffff0000be05f204(0x40,(ulong)uVar1 << 7);
      DAT_ffff0000be0d6618 = FUN_ffff0000be05f204(0x40,(ulong)uVar1 * 0x6d);
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000be0a908f;
      if ((DAT_ffff0000be0d6610 != 0) &&
         (pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000be0a908f, DAT_ffff0000be0d6618 != 0)
         ) {
        bVar4 = true;
        bVar3 = true;
        DAT_ffff0000be0d6620 = 1;
        if (DAT_ffff0000be0d6608 != 0) goto LAB_ffff0000be028458;
        goto LAB_ffff0000be0285c0;
      }
    }
LAB_ffff0000be0285e0:
    FUN_ffff0000be05fd98(pcVar14);
    goto LAB_ffff0000be0285e8;
  }
  bVar3 = DAT_ffff0000be0d6610 != 0;
  bVar4 = DAT_ffff0000be0d6618 != 0;
  if (DAT_ffff0000be0d6608 == 0) {
LAB_ffff0000be0285c0:
    pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000be0c6290;
  }
  else {
LAB_ffff0000be028458:
    if ((!bVar3) || (!bVar4)) goto LAB_ffff0000be0285c0;
    uVar1 = *(uint *)(DAT_ffff0000be0d6608 + 0x50);
    FUN_ffff0000be060ae0(DAT_ffff0000be0d6608,0,*(undefined8 *)(lVar6 + 0x28));
    iVar5 = FUN_ffff0000be027664(lVar6,DAT_ffff0000be0d6608,*(undefined8 *)(lVar6 + 0x28));
    if (iVar5 != 0) {
      FUN_ffff0000be05fd98(s__PART_UTILS__gpt_header_get_fail_ffff0000be0bae59);
      iVar5 = FUN_ffff0000be027664
                        (lVar6,DAT_ffff0000be0d6608,
                         *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000be0bae59;
        goto LAB_ffff0000be028788;
      }
    }
    lVar8 = (ulong)uVar1 * 0x80;
    uVar9 = (ulong)*(uint *)(DAT_ffff0000be0d6608 + 0x50);
    lVar11 = uVar9 * 0x6d;
    lVar13 = uVar9 * 0x80;
    if ((lVar8 + uVar9 * -0x80 != 0) || (uVar1 != *(uint *)(DAT_ffff0000be0d6608 + 0x50))) {
      FUN_ffff0000be05fd98
                (s__PART_UTILS__update_gpt_netry_tl_ffff0000be0af938,lVar8,lVar13,
                 (ulong)uVar1 * 0x6d,lVar11);
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be0d6610);
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be0d6618);
      DAT_ffff0000be0d6610 = FUN_ffff0000be05f204(0x40,lVar13);
      DAT_ffff0000be0d6618 = FUN_ffff0000be05f204(0x40,lVar11);
      if ((DAT_ffff0000be0d6610 == 0) || (DAT_ffff0000be0d6618 == 0)) {
        pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000be0a908f;
LAB_ffff0000be028788:
        FUN_ffff0000be05fd98(pcVar14);
        return -0x1c;
      }
    }
    FUN_ffff0000be060ae0(DAT_ffff0000be0d6610,0,lVar13);
    FUN_ffff0000be060ae0(DAT_ffff0000be0d6618,0,lVar11);
    iVar5 = FUN_ffff0000be027808(lVar6,DAT_ffff0000be0d6608,DAT_ffff0000be0d6610);
    lVar13 = DAT_ffff0000be0d6618;
    lVar11 = DAT_ffff0000be0d6610;
    lVar8 = DAT_ffff0000be0d6608;
    if (((iVar5 == 0) && (DAT_ffff0000be0d6608 != 0)) && (DAT_ffff0000be0d6610 != 0)) {
      DAT_ffff0000be0d6624 = 0;
      if (*(int *)(DAT_ffff0000be0d6608 + 0x50) == 0) {
LAB_ffff0000be028794:
        iVar5 = 0;
      }
      else {
        uVar9 = 0;
        lVar15 = DAT_ffff0000be0d6610 + 0x38;
        lVar16 = DAT_ffff0000be0d6618;
        do {
          pcVar14 = (char *)(lVar11 + uVar9 * 0x80);
          if ((*pcVar14 == '\0') && (pcVar14[1] == '\0')) goto LAB_ffff0000be028794;
          lVar12 = lVar13 + uVar9 * 0x6d;
          uVar10 = 0;
          do {
            sVar2 = *(short *)(lVar15 + uVar10 * 2);
            if (sVar2 == 0) break;
            *(char *)(lVar16 + uVar10) = (char)sVar2;
            uVar10 = uVar10 + 1;
          } while (uVar10 != 0x47);
          *(undefined1 *)(lVar12 + (uVar10 & 0xffffffff)) = 0;
          *(undefined1 *)(lVar12 + 0x47) = 0;
          lVar7 = FUN_ffff0000be027874(lVar11 + uVar9 * 0x80 + 0x10,lVar13 + uVar9 * 0x6d + 0x48);
          if (lVar7 == 0) {
            FUN_ffff0000be05fd98(s__PART_UTILS___s_guid_parse_faile_ffff0000be0bae7d,lVar12);
            iVar5 = -0x16;
            break;
          }
          iVar5 = 0;
          uVar9 = uVar9 + 1;
          lVar16 = lVar16 + 0x6d;
          lVar15 = lVar15 + 0x80;
          DAT_ffff0000be0d6624 = DAT_ffff0000be0d6624 + 1;
        } while (uVar9 < *(uint *)(lVar8 + 0x50));
      }
      goto LAB_ffff0000be0285e8;
    }
    pcVar14 = s__PART_UTILS__get_GPT_failed_ffff0000be0a6daf;
  }
  FUN_ffff0000be05fd98(pcVar14);
  iVar5 = -0x1c;
LAB_ffff0000be0285e8:
  FUN_ffff0000be01a7c8(&LAB_ffff0000be0d6628);
  FUN_ffff0000be05def0(lVar6);
  return iVar5;
}


