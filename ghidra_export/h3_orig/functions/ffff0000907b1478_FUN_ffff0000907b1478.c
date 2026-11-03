// ffff0000907b1478  size=1276  FUN_ffff0000907b1478

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907b1478(long param_1,int param_2)

{
  undefined1 uVar1;
  byte bVar2;
  long lVar3;
  bool bVar4;
  int iVar5;
  undefined8 uVar6;
  undefined4 uVar7;
  ushort uVar8;
  ulong uVar9;
  long lVar10;
  undefined1 *puVar11;
  uint uVar12;
  uint uVar13;
  ulong uVar14;
  byte *pbVar15;
  
  FUN_ffff00009077eb88(&DAT_ffff00009090ce98,0,0x408);
  lVar3 = DAT_ffff000090839a80;
  _DAT_ffff00009090d098 = 0x200;
  _DAT_ffff00009090d29c = 0x200;
  DAT_ffff000090837eb0 = *(int *)(param_1 + 0x3c);
  _DAT_ffff00009090cba8 = (byte *)(param_1 + (ulong)*(uint *)(param_1 + 0x34));
  _DAT_ffff00009090cbb8 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
  uVar6 = FUN_ffff0000907b05b4(DAT_ffff000090839a80 + 0x408,DAT_ffff000090839a80 + 0x40c,1);
  if ((int)uVar6 != 0) {
    return uVar6;
  }
  if (DAT_ffff000090837ea8 == 1) {
    uVar6 = FUN_ffff0000907afe98(param_1,param_2,0x800);
    if ((int)uVar6 != 0) {
      return uVar6;
    }
    uVar7 = *(undefined4 *)(lVar3 + 0x410);
  }
  else {
    uVar7 = 0;
  }
  _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
  _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
  *DAT_ffff000090839b10 = uVar7;
  if (_DAT_ffff00009090ce90 != 0) {
    do {
      iVar5 = FUN_ffff0000907b249c(&DAT_ffff00009090ce98);
      if (iVar5 == 0) break;
    } while (_DAT_ffff00009090ce90 != 0);
    _DAT_ffff00009090ce88 = _DAT_ffff00009090cba8;
    _DAT_ffff00009090ce90 = _DAT_ffff00009090cbb8;
    if (iVar5 == 0) {
      while (_DAT_ffff00009090ce90 != 0) {
        while( true ) {
          iVar5 = FUN_ffff0000907af25c(2);
          if (iVar5 != 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          if (_DAT_ffff00009090ce90 == 0) break;
          bVar2 = _DAT_ffff00009090ce88[1];
          uVar12 = (uint)bVar2;
          if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
            iVar5 = (uVar12 & 0x7f) + 1;
            uVar13 = uVar12 & 0x7f;
            uVar12 = 0;
            pbVar15 = _DAT_ffff00009090ce88 + 2;
            do {
              uVar13 = uVar13 - 1;
              uVar12 = (uint)*pbVar15 | uVar12 << 8;
              pbVar15 = pbVar15 + 1;
            } while (uVar13 != 0);
          }
          else {
            iVar5 = 1;
          }
          if ((*_DAT_ffff00009090ce88 & 0x20) != 0) {
            uVar12 = 0;
          }
          uVar9 = (ulong)(iVar5 + uVar12 + 1);
          bVar4 = _DAT_ffff00009090ce90 < uVar9;
          _DAT_ffff00009090ce90 = _DAT_ffff00009090ce90 - uVar9;
          if (bVar4) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
          if (*_DAT_ffff00009090ce88 == 3) {
            bVar2 = _DAT_ffff00009090ce88[1];
            if (((char)bVar2 < '\0') && (uVar14 = (ulong)bVar2 & 0x7f, (int)uVar14 != 0)) {
              uVar12 = 0;
              lVar10 = uVar14 + 1;
              pbVar15 = _DAT_ffff00009090ce88 + 2;
              do {
                uVar13 = (int)uVar14 - 1;
                uVar14 = (ulong)uVar13;
                uVar12 = (uint)*pbVar15 | uVar12 << 8;
                pbVar15 = pbVar15 + 1;
              } while (uVar13 != 0);
            }
            else {
              uVar12 = (uint)bVar2;
              lVar10 = 1;
            }
            uVar12 = uVar12 - 1;
            if (uVar12 < 0x40d) {
              pbVar15 = _DAT_ffff00009090ce88 + lVar10 + 2;
              _DAT_ffff00009090ce88 = _DAT_ffff00009090ce88 + uVar9;
              FUN_ffff0000907acf74(&DAT_ffff00009090d2a0,pbVar15,uVar12);
              _DAT_ffff00009090ce88 = (byte *)0x0;
              _DAT_ffff00009090ce90 = 0;
              if (_DAT_ffff00009090d2a8 == 0) {
                uVar6 = FUN_ffff0000907afd28(&DAT_ffff00009090d2a0,uVar12,&DAT_ffff00009090ce98);
                goto LAB_ffff0000907b1840;
              }
              uVar13 = _DAT_ffff00009090d29c + 1 >> 1;
              if (uVar13 != _DAT_ffff00009090d2a0) {
                _DAT_ffff00009090ce88 = (byte *)0x0;
                _DAT_ffff00009090ce90 = 0;
                return 0x7015;
              }
              if (1 < _DAT_ffff00009090d29c + 1) {
                lVar10 = 0;
                do {
                  if ((_DAT_ffff00009090d29c & 1) == 0) {
                    uVar8 = *(ushort *)(&DAT_ffff00009090d09c + lVar10) >> 8 |
                            *(ushort *)(&DAT_ffff00009090d09c + lVar10) << 8;
                  }
                  else {
                    uVar8 = (ushort)DAT_ffff00009090d09c;
                    if (lVar10 != 0) {
                      uVar8 = CONCAT11((&DAT_ffff00009090d09c)[(int)lVar10 - 1],
                                       (&DAT_ffff00009090d09c)[lVar10]);
                    }
                  }
                  if (*(ushort *)(lVar10 + -0xffff6f6f2d54) != uVar8) {
                    _DAT_ffff00009090ce88 = (byte *)0x0;
                    _DAT_ffff00009090ce90 = 0;
                    return 0x7016;
                  }
                  lVar10 = lVar10 + 2;
                } while ((ulong)uVar13 << 1 != lVar10);
              }
              uVar13 = _DAT_ffff00009090d098 + 1 >> 1;
              if (uVar13 == _DAT_ffff00009090d2a4) {
                if (1 < _DAT_ffff00009090d098 + 1) {
                  lVar10 = 0;
                  do {
                    if ((_DAT_ffff00009090d098 & 1) == 0) {
                      uVar8 = *(ushort *)(&DAT_ffff00009090ce98 + lVar10) >> 8 |
                              *(ushort *)(&DAT_ffff00009090ce98 + lVar10) << 8;
                    }
                    else {
                      uVar8 = (ushort)DAT_ffff00009090ce98;
                      if (lVar10 != 0) {
                        uVar8 = CONCAT11((&DAT_ffff00009090ce98)[(int)lVar10 - 1],
                                         (&DAT_ffff00009090ce98)[lVar10]);
                      }
                    }
                    if (*(ushort *)(lVar10 + -0xffff6f6f2c54) != uVar8) {
                      _DAT_ffff00009090ce88 = (byte *)0x0;
                      _DAT_ffff00009090ce90 = 0;
                      return 0x7018;
                    }
                    lVar10 = lVar10 + 2;
                  } while ((ulong)uVar13 << 1 != lVar10);
                }
                uVar6 = FUN_ffff0000907af8d4(&DAT_ffff00009090d2a0,uVar12,&DAT_ffff00009090ce98);
LAB_ffff0000907b1840:
                uVar12 = _DAT_ffff00009090d098;
                if ((int)uVar6 == 0) {
                  iVar5 = FUN_ffff0000907a28bc(_DAT_ffff00009090d098);
                  if (iVar5 == 0) {
                    uVar6 = 0x101026;
                  }
                  else {
                    FUN_ffff00009077eb88(&DAT_ffff00009090cc00,0,0x200);
                    FUN_ffff0000907acf74(&DAT_ffff00009090cc00,&DAT_ffff00009090ce98,uVar12);
                    _DAT_ffff00009090ce00 = uVar12;
                    if (1 < uVar12) {
                      uVar9 = (ulong)(uVar12 >> 1);
                      puVar11 = &DAT_ffff00009090cc00;
                      do {
                        uVar12 = uVar12 - 1;
                        uVar9 = uVar9 - 1;
                        uVar1 = *puVar11;
                        *puVar11 = (&DAT_ffff00009090cc00)[uVar12];
                        (&DAT_ffff00009090cc00)[uVar12] = uVar1;
                        puVar11 = puVar11 + 1;
                      } while (uVar9 != 0);
                    }
                    uVar6 = FUN_ffff0000907b1974();
                    if ((int)uVar6 == 0) {
                      _DAT_ffff00009090cbb8 = (ulong)_DAT_ffff00009090cbc0;
                      _DAT_ffff00009090cba8 = _DAT_ffff00009090cbb0;
                      uVar6 = FUN_ffff0000907afe98(param_1,param_2,0xf);
                      if (((int)uVar6 == 0) && (DAT_ffff000090837eb0 == 0x2000001)) {
                        _DAT_ffff00009090cba8 = (byte *)(param_1 + (ulong)*(uint *)(param_1 + 0x34))
                        ;
                        _DAT_ffff00009090cbb8 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
                        uVar6 = FUN_ffff0000907af738(lVar3 + 0x418,0x30,lVar3 + 0x448);
                        return uVar6;
                      }
                    }
                  }
                }
              }
              else {
                uVar6 = 0x7017;
              }
              return uVar6;
            }
          }
          _DAT_ffff00009090ce88 = _DAT_ffff00009090ce88 + uVar9;
          if (_DAT_ffff00009090ce90 == 0) {
            _DAT_ffff00009090ce88 = (byte *)0x0;
            _DAT_ffff00009090ce90 = 0;
            return 0x7014;
          }
        }
        FUN_ffff00009077de54(s___s__end_of_cert__not_found__ffff0000907fab80,&DAT_ffff0000907f20fe);
      }
    }
  }
  _DAT_ffff00009090ce88 = (byte *)0x0;
  _DAT_ffff00009090ce90 = 0;
  return 0x7014;
}


