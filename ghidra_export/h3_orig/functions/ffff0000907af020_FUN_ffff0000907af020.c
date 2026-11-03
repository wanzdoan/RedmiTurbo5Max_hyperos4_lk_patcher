// ffff0000907af020  size=572  FUN_ffff0000907af020

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907af020(undefined8 param_1,uint param_2,uint *param_3)

{
  byte bVar1;
  bool bVar2;
  uint uVar3;
  int iVar4;
  byte *pbVar5;
  byte *pbVar6;
  uint uVar7;
  ulong uVar8;
  long lVar9;
  byte *pbVar10;
  uint uVar11;
  
  *param_3 = 0;
  _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
  _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
  if (_DAT_ffff00009090cbb8 == 0) {
    uVar3 = 0;
  }
  else {
    uVar11 = 0x101028;
    do {
      uVar3 = FUN_ffff0000907af25c(0xe);
      if (uVar3 != 0) {
        iVar4 = FUN_ffff0000907ad53c();
        if (iVar4 == 0) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return uVar3;
        }
        FUN_ffff00009077de54(s_Failed_in_asn1_get_oid__ffff00009080330c);
        _DAT_ffff00009090ce88 = (byte *)0x0;
        _DAT_ffff00009090ce90 = 0;
        return uVar3;
      }
      if (_DAT_ffff00009090ce90 == 0) {
        FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20fe);
        uVar11 = 0x700e;
        iVar4 = FUN_ffff0000907ad53c();
joined_r0xffff0000907af1e4:
        if (iVar4 == 0) {
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return uVar11;
        }
        FUN_ffff00009077de54(s_Failed_in_asn1_get_next_obj_ffff0000907da1c0);
        _DAT_ffff00009090ce88 = (byte *)0x0;
        _DAT_ffff00009090ce90 = 0;
        return uVar11;
      }
      pbVar5 = _DAT_ffff00009090ce88 + 1;
      bVar1 = *pbVar5;
      uVar3 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar3 & 0x7f) + 1;
        uVar7 = uVar3 & 0x7f;
        uVar3 = 0;
        pbVar10 = _DAT_ffff00009090ce88 + 2;
        do {
          uVar7 = uVar7 - 1;
          uVar3 = (uint)*pbVar10 | uVar3 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar7 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
        uVar3 = 0;
      }
      uVar8 = (ulong)(iVar4 + uVar3 + 1);
      bVar2 = _DAT_ffff00009090ce90 < uVar8;
      _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar8;
      if (bVar2) {
        _DAT_ffff00009090ce90 = 0;
        iVar4 = FUN_ffff0000907ad53c();
        goto joined_r0xffff0000907af1e4;
      }
      pbVar10 = _DAT_ffff00009090ce88 + uVar8;
      if (*_DAT_ffff00009090ce88 != 0x13) {
        uVar11 = 0x700f;
        _DAT_ffff00009090ce88 = pbVar10;
        iVar4 = FUN_ffff0000907ad53c();
        goto joined_r0xffff0000907af1e4;
      }
      if (param_2 == 0) {
        uVar3 = 9;
        _DAT_ffff00009090ce88 = pbVar10;
      }
      else {
        bVar1 = *pbVar5;
        if (((char)bVar1 < '\0') && (uVar8 = (ulong)bVar1 & 0x7f, (int)uVar8 != 0)) {
          uVar7 = 0;
          lVar9 = uVar8 + 1;
          pbVar6 = _DAT_ffff00009090ce88 + 2;
          do {
            uVar3 = (int)uVar8 - 1;
            uVar8 = (ulong)uVar3;
            uVar7 = (uint)*pbVar6 | uVar7 << 8;
            pbVar6 = pbVar6 + 1;
          } while (uVar3 != 0);
        }
        else {
          uVar7 = (uint)bVar1;
          lVar9 = 1;
        }
        uVar3 = 0x1028;
        _DAT_ffff00009090ce88 = pbVar10;
        *param_3 = uVar7;
        if (uVar7 <= param_2) {
          FUN_ffff0000907acf74(param_1,pbVar5 + lVar9,uVar7);
          _DAT_ffff00009090ce88 = (byte *)0x0;
          _DAT_ffff00009090ce90 = 0;
          return 0;
        }
      }
      uVar3 = uVar3 | 0x100000;
      iVar4 = FUN_ffff0000907ad53c();
      if (iVar4 != 0) {
        FUN_ffff00009077de54(s_Parse_socid_in_printable_string_f_ffff0000907defa0);
      }
    } while (_DAT_ffff00009090ce90 != 0);
  }
  iVar4 = FUN_ffff0000907ad53c();
  if (iVar4 != 0) {
    FUN_ffff00009077de54(s_Image_is_not_signed_with_socid__ffff0000907d06a5);
  }
  _DAT_ffff00009090ce88 = (byte *)0x0;
  _DAT_ffff00009090ce90 = 0;
  return uVar3;
}


