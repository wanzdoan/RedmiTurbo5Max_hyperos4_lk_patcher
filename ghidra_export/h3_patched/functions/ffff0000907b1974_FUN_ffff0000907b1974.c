// ffff0000907b1974  size=1508  FUN_ffff0000907b1974

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b1974(void)

{
  byte *pbVar1;
  undefined1 uVar2;
  byte bVar3;
  undefined4 *puVar4;
  bool bVar5;
  int iVar6;
  byte *pbVar7;
  long lVar8;
  uint uVar9;
  ulong uVar10;
  int iVar11;
  uint uVar12;
  undefined1 *puVar13;
  uint uVar14;
  byte *pbVar15;
  ulong uVar16;
  uint uVar17;
  byte *pbVar18;
  undefined1 auStack_6c8 [512];
  undefined8 local_4c8;
  undefined1 local_4c0 [512];
  undefined1 local_2c0 [560];
  undefined8 local_90;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 local_58;
  
  FUN_ffff00009077eb88(auStack_6c8,0,0x200);
  pbVar7 = _DAT_ffff00009090cba8;
  uVar10 = _DAT_ffff00009090cbb8;
  do {
    while( true ) {
      while( true ) {
        do {
          pbVar18 = pbVar7;
          if (uVar10 == 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          bVar3 = pbVar18[1];
          uVar12 = (uint)bVar3;
          if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
            iVar11 = (uVar12 & 0x7f) + 1;
            uVar9 = uVar12 & 0x7f;
            uVar12 = 0;
            pbVar7 = pbVar18 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar12 = (uint)*pbVar7 | uVar12 << 8;
              pbVar7 = pbVar7 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar11 = 1;
          }
          if ((*pbVar18 & 0x20) != 0) {
            uVar12 = 0;
          }
          uVar16 = (ulong)(iVar11 + uVar12 + 1);
          _DAT_ffff00009090ce90 = uVar10 - uVar16;
          if (uVar10 < uVar16) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          pbVar1 = pbVar18 + uVar16;
          pbVar7 = pbVar1;
          uVar10 = _DAT_ffff00009090ce90;
        } while (*pbVar18 != 0x30);
        if (_DAT_ffff00009090ce90 != 0) break;
        _DAT_ffff00009090ce88 = pbVar1;
        FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20e7);
        pbVar7 = _DAT_ffff00009090ce88;
        uVar10 = _DAT_ffff00009090ce90;
      }
      bVar3 = pbVar1[1];
      uVar9 = (uint)bVar3;
      if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
        uVar17 = uVar9 & 0x7f;
        iVar6 = (uVar9 & 0x7f) + 1;
        uVar9 = 0;
        pbVar15 = pbVar18 + uVar16 + 2;
        do {
          uVar17 = uVar17 - 1;
          uVar9 = (uint)*pbVar15 | uVar9 << 8;
          pbVar15 = pbVar15 + 1;
        } while (uVar17 != 0);
      }
      else {
        iVar6 = 1;
      }
      uVar16 = (ulong)(iVar6 + uVar9 + 1);
      uVar10 = _DAT_ffff00009090ce90 - uVar16;
      if (uVar16 <= _DAT_ffff00009090ce90) break;
      uVar10 = 0;
    }
    pbVar7 = pbVar1 + uVar16;
  } while (*pbVar1 != 0x30);
  bVar3 = pbVar1[1];
  uVar9 = (uint)bVar3;
  if (((char)bVar3 < '\0') && (uVar17 = uVar9 & 0x7f, (bVar3 & 0x7f) != 0)) {
    uVar14 = uVar9 & 0x7f;
    uVar9 = 0;
    pbVar7 = pbVar18 + (ulong)(uVar12 + iVar11 + 1) + 2;
    do {
      uVar14 = uVar14 - 1;
      uVar9 = (uint)*pbVar7 | uVar9 << 8;
      pbVar7 = pbVar7 + 1;
    } while (uVar14 != 0);
    iVar11 = uVar17 + 2;
  }
  else {
    iVar11 = 2;
  }
  if (_DAT_ffff00009090cbb8 != 0) {
    iVar11 = uVar9 + iVar11;
    _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
    do {
      iVar6 = FUN_ffff0000907af25c(0);
      if (iVar6 != 0) {
        _DAT_ffff00009090ce88 = (byte *)0x0;
        _DAT_ffff00009090ce90 = 0;
        return 0x7014;
      }
      pbVar7 = _DAT_ffff00009090ce88;
      if (_DAT_ffff00009090ce90 == 0) {
LAB_ffff0000907b1c4c:
        _DAT_ffff00009090ce88 = pbVar7;
        FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20e7);
        pbVar18 = _DAT_ffff00009090ce88;
      }
      else {
        bVar3 = _DAT_ffff00009090ce88[1];
        uVar12 = (uint)bVar3;
        if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
          iVar6 = (uVar12 & 0x7f) + 1;
          uVar9 = uVar12 & 0x7f;
          uVar12 = 0;
          pbVar7 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar9 = uVar9 - 1;
            uVar12 = (uint)*pbVar7 | uVar12 << 8;
            pbVar7 = pbVar7 + 1;
          } while (uVar9 != 0);
        }
        else {
          iVar6 = 1;
        }
        uVar10 = (ulong)(iVar6 + uVar12 + 1);
        bVar5 = _DAT_ffff00009090ce90 < uVar10;
        _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar10;
        if (bVar5) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return 0x7014;
        }
        pbVar7 = _DAT_ffff00009090ce88 + uVar10;
        pbVar18 = pbVar7;
        if (*_DAT_ffff00009090ce88 == 0x30) {
          if (_DAT_ffff00009090ce90 == 0) goto LAB_ffff0000907b1c4c;
          bVar3 = pbVar7[1];
          uVar12 = (uint)bVar3;
          if (((char)bVar3 < '\0') && ((bVar3 & 0x7f) != 0)) {
            uVar9 = uVar12 & 0x7f;
            iVar6 = (uVar12 & 0x7f) + 1;
            uVar12 = 0;
            pbVar18 = _DAT_ffff00009090ce88 + uVar10 + 2;
            do {
              uVar9 = uVar9 - 1;
              uVar12 = (uint)*pbVar18 | uVar12 << 8;
              pbVar18 = pbVar18 + 1;
            } while (uVar9 != 0);
          }
          else {
            iVar6 = 1;
          }
          if ((*pbVar7 & 0x20) != 0) {
            uVar12 = 0;
          }
          uVar16 = (ulong)(iVar6 + uVar12 + 1);
          bVar5 = _DAT_ffff00009090ce90 < uVar16;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar16;
          if (bVar5) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          pbVar18 = pbVar7 + uVar16;
          if (*pbVar7 == 3) {
            bVar3 = pbVar7[1];
            if (((char)bVar3 < '\0') && (uVar16 = (ulong)bVar3 & 0x7f, (int)uVar16 != 0)) {
              uVar12 = 0;
              lVar8 = uVar16 + 1;
              pbVar15 = _DAT_ffff00009090ce88 + uVar10 + 2;
              do {
                uVar9 = (int)uVar16 - 1;
                uVar16 = (ulong)uVar9;
                uVar12 = (uint)*pbVar15 | uVar12 << 8;
                pbVar15 = pbVar15 + 1;
              } while (uVar9 != 0);
            }
            else {
              uVar12 = (uint)bVar3;
              lVar8 = 1;
            }
            uVar12 = uVar12 - 1;
            if (uVar12 < 0x201) {
              _DAT_ffff00009090ce88 = pbVar18;
              FUN_ffff0000907acf74(auStack_6c8,pbVar7 + lVar8 + 2,uVar12);
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              local_58 = 0;
              local_60 = 0;
              local_68 = 0;
              local_70 = 0;
              local_78 = 0;
              local_80 = 0;
              local_88 = 0;
              local_90 = 0;
              FUN_ffff00009077eb88(local_2c0,0,0x200);
              FUN_ffff00009077eb88(local_4c0,0,0x200);
              puVar4 = DAT_ffff000090839ac8;
              local_4c8 = 0;
              iVar6 = FUN_ffff0000907a262c(pbVar1,iVar11,&local_90,*DAT_ffff000090839ac8);
              if (iVar6 != 0) {
                return 0x701d;
              }
              if (DAT_ffff000090837ea0 != uVar12) {
                return 0x7022;
              }
              FUN_ffff0000907acf74(local_2c0,auStack_6c8,uVar12);
              if (1 < DAT_ffff000090837ea0) {
                uVar10 = (ulong)(DAT_ffff000090837ea0 >> 1);
                puVar13 = local_2c0;
                uVar9 = DAT_ffff000090837ea0;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = uVar10 - 1;
                  uVar2 = *puVar13;
                  *puVar13 = local_2c0[uVar9];
                  local_2c0[uVar9] = uVar2;
                  puVar13 = puVar13 + 1;
                } while (uVar10 != 0);
              }
              FUN_ffff0000907a2774(local_2c0,&DAT_ffff00009090cc00,local_4c0,*puVar4);
              if (1 < DAT_ffff000090837ea0) {
                uVar10 = (ulong)(DAT_ffff000090837ea0 >> 1);
                puVar13 = local_4c0;
                uVar9 = DAT_ffff000090837ea0;
                do {
                  uVar9 = uVar9 - 1;
                  uVar10 = uVar10 - 1;
                  uVar2 = *puVar13;
                  *puVar13 = local_4c0[uVar9];
                  local_4c0[uVar9] = uVar2;
                  puVar13 = puVar13 + 1;
                } while (uVar10 != 0);
              }
              iVar6 = FUN_ffff0000907a27a4(&local_90,local_4c0,(long)&local_4c8 + 4,*puVar4);
              if ((iVar6 != 0) &&
                 (iVar6 = FUN_ffff0000907b275c(pbVar1,iVar11,auStack_6c8,uVar12), iVar6 != 0)) {
                return 0x701e;
              }
              if ((local_4c8._4_4_ != 1) &&
                 (iVar6 = FUN_ffff0000907b275c(pbVar1,iVar11,auStack_6c8,uVar12), iVar6 != 0)) {
                return 0x701f;
              }
              if (_DAT_ffff00009090c514 != 0) {
                FUN_ffff000090737364(&local_4c8);
                FUN_ffff00009077cbd8((uint)local_4c8 % 0x14);
                iVar6 = FUN_ffff0000907a27a4(&local_90,local_4c0,(long)&local_4c8 + 4,*puVar4);
                if (iVar6 != 0) {
                  return 0x701e;
                }
                if (local_4c8._4_4_ != 1) {
                  return 0x701f;
                }
              }
              _DAT_ffff00009090cbb0 = pbVar1;
              _DAT_ffff00009090cbc0 = iVar11;
              return 0;
            }
          }
        }
      }
      _DAT_ffff00009090ce88 = pbVar18;
    } while (_DAT_ffff00009090ce90 != 0);
  }
  _DAT_ffff00009090ce90 = 0;
  _DAT_ffff00009090ce88 = (byte *)0x0;
  return 0x7014;
}


