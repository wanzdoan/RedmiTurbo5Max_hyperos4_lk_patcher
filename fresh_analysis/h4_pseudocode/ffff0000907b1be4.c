
ulong FUN_ffff0000907b1be4(long param_1,uint param_2)

{
  undefined1 uVar1;
  byte bVar2;
  undefined *puVar3;
  bool bVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  undefined4 uVar8;
  ushort uVar9;
  long lVar10;
  undefined1 *puVar11;
  uint uVar12;
  ulong uVar13;
  byte *pbVar14;
  uint local_6c;
  byte *local_68;
  
  uVar5 = 0x100001;
  FUN_ffff00009077ee50(&DAT_ffff00009090cf58,0,0x408);
  local_68 = (byte *)0x0;
  DAT_ffff00009090d158 = 0x200;
  DAT_ffff00009090d35c = 0x200;
  uVar12 = *(uint *)(param_1 + 0x34);
  local_6c = 0;
  DAT_ffff000090837ed0 = *(int *)(param_1 + 0x3c);
  DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar12);
  DAT_ffff00009090cc78 = (ulong)(param_2 - uVar12);
  if (param_2 < uVar12 || param_2 - uVar12 == 0) {
    uVar12 = 1;
LAB_ffff0000907b1d18:
    return (ulong)(uVar12 | 0x100000);
  }
  uVar7 = FUN_ffff0000907b05cc(&local_68,&local_6c);
  puVar3 = PTR_DAT_ffff000090839aa0;
  if ((int)uVar7 != 0) {
    return uVar7;
  }
  DAT_ffff00009090cc78 = (ulong)local_6c;
  DAT_ffff00009090cc68 = local_68;
  uVar7 = FUN_ffff0000907b0968(PTR_DAT_ffff000090839aa0 + 0x408,PTR_DAT_ffff000090839aa0 + 0x40c,1);
  if ((int)uVar7 != 0) {
    return uVar7;
  }
  if (DAT_ffff000090837ec8 != 1) {
    uVar8 = 0;
LAB_ffff0000907b1d44:
    DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
    DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
    *(undefined4 *)PTR_DAT_ffff000090839b30 = uVar8;
    if (DAT_ffff00009090cf50 != 0) {
      do {
        iVar6 = FUN_ffff0000907b2b0c(&DAT_ffff00009090cf58);
        if (iVar6 == 0) break;
      } while (DAT_ffff00009090cf50 != 0);
      DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
      DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
      if (iVar6 == 0) {
        while (DAT_ffff00009090cf50 != 0) {
          while( true ) {
            iVar6 = FUN_ffff0000907af524(2);
            if (iVar6 != 0) {
              DAT_ffff00009090cf48 = (byte *)0x0;
              DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
            if (DAT_ffff00009090cf50 == 0) break;
            bVar2 = DAT_ffff00009090cf48[1];
            uVar12 = (uint)bVar2;
            if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
              iVar6 = (uVar12 & 0x7f) + 1;
              uVar5 = uVar12 & 0x7f;
              uVar12 = 0;
              pbVar14 = DAT_ffff00009090cf48 + 2;
              do {
                uVar5 = uVar5 - 1;
                uVar12 = (uint)*pbVar14 | uVar12 << 8;
                pbVar14 = pbVar14 + 1;
              } while (uVar5 != 0);
            }
            else {
              iVar6 = 1;
            }
            if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
              uVar12 = 0;
            }
            uVar7 = (ulong)(iVar6 + uVar12 + 1);
            bVar4 = DAT_ffff00009090cf50 < uVar7;
            DAT_ffff00009090cf50 = DAT_ffff00009090cf50 - uVar7;
            if (bVar4) {
              DAT_ffff00009090cf48 = (byte *)0x0;
              DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
            if (*DAT_ffff00009090cf48 == 3) {
              bVar2 = DAT_ffff00009090cf48[1];
              if (((char)bVar2 < '\0') && (uVar13 = (ulong)bVar2 & 0x7f, (int)uVar13 != 0)) {
                uVar12 = 0;
                lVar10 = uVar13 + 1;
                pbVar14 = DAT_ffff00009090cf48 + 2;
                do {
                  uVar5 = (int)uVar13 - 1;
                  uVar13 = (ulong)uVar5;
                  uVar12 = (uint)*pbVar14 | uVar12 << 8;
                  pbVar14 = pbVar14 + 1;
                } while (uVar5 != 0);
              }
              else {
                uVar12 = (uint)bVar2;
                lVar10 = 1;
              }
              uVar12 = uVar12 - 1;
              if (uVar12 < 0x40d) {
                pbVar14 = DAT_ffff00009090cf48 + lVar10 + 2;
                DAT_ffff00009090cf48 = DAT_ffff00009090cf48 + uVar7;
                FUN_ffff0000907ad234(&DAT_ffff00009090d360,pbVar14,uVar12);
                DAT_ffff00009090cf48 = (byte *)0x0;
                DAT_ffff00009090cf50 = 0;
                if (DAT_ffff00009090d368 == 0) {
                  uVar7 = FUN_ffff0000907afff0(&DAT_ffff00009090d360,uVar12,&DAT_ffff00009090cf58);
                }
                else {
                  uVar5 = DAT_ffff00009090d35c + 1 >> 1;
                  if (uVar5 != DAT_ffff00009090d360) {
                    DAT_ffff00009090cf48 = (byte *)0x0;
                    DAT_ffff00009090cf50 = 0;
                    return 0x7015;
                  }
                  if (1 < DAT_ffff00009090d35c + 1) {
                    lVar10 = 0;
                    do {
                      if ((DAT_ffff00009090d35c & 1) == 0) {
                        uVar9 = *(ushort *)((long)&DAT_ffff00009090d15c + lVar10) >> 8 |
                                *(ushort *)((long)&DAT_ffff00009090d15c + lVar10) << 8;
                      }
                      else {
                        uVar9 = (ushort)(byte)DAT_ffff00009090d15c;
                        if (lVar10 != 0) {
                          uVar9 = CONCAT11(*(undefined1 *)
                                            ((long)&DAT_ffff00009090d15c + (ulong)((int)lVar10 - 1))
                                           ,*(undefined1 *)((long)&DAT_ffff00009090d15c + lVar10));
                        }
                      }
                      if (*(ushort *)((long)&DAT_ffff00009090d36c + lVar10) != uVar9) {
                        DAT_ffff00009090cf48 = (byte *)0x0;
                        DAT_ffff00009090cf50 = 0;
                        return 0x7016;
                      }
                      lVar10 = lVar10 + 2;
                    } while ((ulong)uVar5 << 1 != lVar10);
                  }
                  uVar5 = DAT_ffff00009090d158 + 1 >> 1;
                  if (uVar5 != DAT_ffff00009090d364) {
                    DAT_ffff00009090cf48 = (byte *)0x0;
                    DAT_ffff00009090cf50 = 0;
                    return 0x7017;
                  }
                  if (1 < DAT_ffff00009090d158 + 1) {
                    lVar10 = 0;
                    do {
                      if ((DAT_ffff00009090d158 & 1) == 0) {
                        uVar9 = *(ushort *)((long)&DAT_ffff00009090cf58 + lVar10) >> 8 |
                                *(ushort *)((long)&DAT_ffff00009090cf58 + lVar10) << 8;
                      }
                      else {
                        uVar9 = (ushort)(byte)DAT_ffff00009090cf58;
                        if (lVar10 != 0) {
                          uVar9 = CONCAT11(*(undefined1 *)
                                            ((long)&DAT_ffff00009090cf58 + (ulong)((int)lVar10 - 1))
                                           ,*(undefined1 *)((long)&DAT_ffff00009090cf58 + lVar10));
                        }
                      }
                      if (*(ushort *)((long)&DAT_ffff00009090d46c + lVar10) != uVar9) {
                        DAT_ffff00009090cf48 = (byte *)0x0;
                        DAT_ffff00009090cf50 = 0;
                        return 0x7018;
                      }
                      lVar10 = lVar10 + 2;
                    } while ((ulong)uVar5 << 1 != lVar10);
                  }
                  uVar7 = FUN_ffff0000907afb9c(&DAT_ffff00009090d360,uVar12,&DAT_ffff00009090cf58);
                }
                uVar12 = DAT_ffff00009090d158;
                if ((int)uVar7 != 0) {
                  return uVar7;
                }
                iVar6 = FUN_ffff0000907a2b7c(DAT_ffff00009090d158);
                if (iVar6 != 0) {
                  FUN_ffff00009077ee50(&DAT_ffff00009090ccc0,0,0x200);
                  FUN_ffff0000907ad234(&DAT_ffff00009090ccc0,&DAT_ffff00009090cf58,uVar12);
                  DAT_ffff00009090cec0 = uVar12;
                  if (1 < uVar12) {
                    uVar7 = (ulong)(uVar12 >> 1);
                    puVar11 = &DAT_ffff00009090ccc0;
                    do {
                      uVar12 = uVar12 - 1;
                      uVar7 = uVar7 - 1;
                      uVar1 = *puVar11;
                      *puVar11 = (&DAT_ffff00009090ccc0)[uVar12];
                      (&DAT_ffff00009090ccc0)[uVar12] = uVar1;
                      puVar11 = puVar11 + 1;
                    } while (uVar7 != 0);
                  }
                  DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)*(uint *)(param_1 + 0x34));
                  DAT_ffff00009090cc78 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
                  uVar7 = FUN_ffff0000907b216c();
                  if ((int)uVar7 != 0) {
                    return uVar7;
                  }
                  DAT_ffff00009090cc78 = (ulong)DAT_ffff00009090cc80;
                  DAT_ffff00009090cc68 = (byte *)DAT_ffff00009090cc70;
                  uVar7 = FUN_ffff0000907b0160(param_1,param_2,0xf);
                  if ((int)uVar7 != 0) {
                    return uVar7;
                  }
                  if (DAT_ffff000090837ed0 == 0x2000001) {
                    uVar7 = FUN_ffff0000907b0160(param_1,param_2,0x10);
                    return uVar7;
                  }
                  return uVar7;
                }
                uVar12 = 0x1026;
                goto LAB_ffff0000907b1d18;
              }
            }
            DAT_ffff00009090cf48 = DAT_ffff00009090cf48 + uVar7;
            if (DAT_ffff00009090cf50 == 0) {
              DAT_ffff00009090cf48 = (byte *)0x0;
              DAT_ffff00009090cf50 = 0;
              return 0x7014;
            }
          }
          FUN_ffff00009077e11c
                    (s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        }
      }
    }
    DAT_ffff00009090cf48 = (byte *)0x0;
    DAT_ffff00009090cf50 = 0;
    return 0x7014;
  }
  uVar12 = *(uint *)(param_1 + 0x34);
  local_68 = (byte *)0x0;
  local_6c = 0;
  DAT_ffff00009090cc68 = (byte *)(param_1 + (ulong)uVar12);
  DAT_ffff00009090cc78 = (ulong)(param_2 - uVar12);
  if ((uVar12 <= param_2 && param_2 - uVar12 != 0) &&
     (uVar5 = FUN_ffff0000907b05cc(&local_68,&local_6c), uVar5 == 0)) {
    DAT_ffff00009090cc78 = (ulong)local_6c;
    DAT_ffff00009090cc68 = local_68;
    uVar5 = FUN_ffff0000907b0d14();
    if (uVar5 == 0) {
      uVar8 = *(undefined4 *)(puVar3 + 0x410);
      goto LAB_ffff0000907b1d44;
    }
  }
  return (ulong)uVar5;
}


