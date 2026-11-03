
ulong FUN_ffff0000be08bdd8(long param_1,uint param_2)

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
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b7418,0,0x408);
  local_68 = (byte *)0x0;
  DAT_ffff0000be1b7618 = 0x200;
  DAT_ffff0000be1b781c = 0x200;
  uVar12 = *(uint *)(param_1 + 0x34);
  local_6c = 0;
  DAT_ffff0000be0f40a8 = *(int *)(param_1 + 0x3c);
  DAT_ffff0000be1b7160 = (byte *)(param_1 + (ulong)uVar12);
  DAT_ffff0000be1b7170 = (ulong)(param_2 - uVar12);
  if (param_2 < uVar12 || param_2 - uVar12 == 0) {
    uVar12 = 1;
LAB_ffff0000be08bf0c:
    return (ulong)(uVar12 | 0x100000);
  }
  uVar7 = FUN_ffff0000be08a7c0(&local_68,&local_6c);
  puVar3 = PTR_DAT_ffff0000be0f5720;
  if ((int)uVar7 != 0) {
    return uVar7;
  }
  DAT_ffff0000be1b7170 = (ulong)local_6c;
  DAT_ffff0000be1b7160 = local_68;
  uVar7 = FUN_ffff0000be08ab5c(PTR_DAT_ffff0000be0f5720 + 0x408,PTR_DAT_ffff0000be0f5720 + 0x40c,1);
  if ((int)uVar7 != 0) {
    return uVar7;
  }
  if (DAT_ffff0000be0f40a0 != 1) {
    uVar8 = 0;
LAB_ffff0000be08bf38:
    DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
    DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
    *(undefined4 *)PTR_DAT_ffff0000be0f5798 = uVar8;
    if (DAT_ffff0000be1b7410 != 0) {
      do {
        iVar6 = FUN_ffff0000be08cd00(&DAT_ffff0000be1b7418);
        if (iVar6 == 0) break;
      } while (DAT_ffff0000be1b7410 != 0);
      DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
      DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
      if (iVar6 == 0) {
        while (DAT_ffff0000be1b7410 != 0) {
          while( true ) {
            iVar6 = FUN_ffff0000be089718(2);
            if (iVar6 != 0) {
              DAT_ffff0000be1b7408 = (byte *)0x0;
              DAT_ffff0000be1b7410 = 0;
              return 0x7014;
            }
            if (DAT_ffff0000be1b7410 == 0) break;
            bVar2 = DAT_ffff0000be1b7408[1];
            uVar12 = (uint)bVar2;
            if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
              iVar6 = (uVar12 & 0x7f) + 1;
              uVar5 = uVar12 & 0x7f;
              uVar12 = 0;
              pbVar14 = DAT_ffff0000be1b7408 + 2;
              do {
                uVar5 = uVar5 - 1;
                uVar12 = (uint)*pbVar14 | uVar12 << 8;
                pbVar14 = pbVar14 + 1;
              } while (uVar5 != 0);
            }
            else {
              iVar6 = 1;
            }
            if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
              uVar12 = 0;
            }
            uVar7 = (ulong)(iVar6 + uVar12 + 1);
            bVar4 = DAT_ffff0000be1b7410 < uVar7;
            DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar7;
            if (bVar4) {
              DAT_ffff0000be1b7408 = (byte *)0x0;
              DAT_ffff0000be1b7410 = 0;
              return 0x7014;
            }
            if (*DAT_ffff0000be1b7408 == 3) {
              bVar2 = DAT_ffff0000be1b7408[1];
              if (((char)bVar2 < '\0') && (uVar13 = (ulong)bVar2 & 0x7f, (int)uVar13 != 0)) {
                uVar12 = 0;
                lVar10 = uVar13 + 1;
                pbVar14 = DAT_ffff0000be1b7408 + 2;
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
                pbVar14 = DAT_ffff0000be1b7408 + lVar10 + 2;
                DAT_ffff0000be1b7408 = DAT_ffff0000be1b7408 + uVar7;
                FUN_ffff0000be087428(&DAT_ffff0000be1b7820,pbVar14,uVar12);
                DAT_ffff0000be1b7408 = (byte *)0x0;
                DAT_ffff0000be1b7410 = 0;
                if (DAT_ffff0000be1b7828 == 0) {
                  uVar7 = FUN_ffff0000be08a1e4(&DAT_ffff0000be1b7820,uVar12,&DAT_ffff0000be1b7418);
                }
                else {
                  uVar5 = DAT_ffff0000be1b781c + 1 >> 1;
                  if (uVar5 != DAT_ffff0000be1b7820) {
                    DAT_ffff0000be1b7408 = (byte *)0x0;
                    DAT_ffff0000be1b7410 = 0;
                    return 0x7015;
                  }
                  if (1 < DAT_ffff0000be1b781c + 1) {
                    lVar10 = 0;
                    do {
                      if ((DAT_ffff0000be1b781c & 1) == 0) {
                        uVar9 = *(ushort *)(&DAT_ffff0000be1b761c + lVar10) >> 8 |
                                *(ushort *)(&DAT_ffff0000be1b761c + lVar10) << 8;
                      }
                      else {
                        uVar9 = (ushort)DAT_ffff0000be1b761c;
                        if (lVar10 != 0) {
                          uVar9 = CONCAT11((&DAT_ffff0000be1b761c)[(int)lVar10 - 1],
                                           (&DAT_ffff0000be1b761c)[lVar10]);
                        }
                      }
                      if (*(ushort *)((long)&DAT_ffff0000be1b782c + lVar10) != uVar9) {
                        DAT_ffff0000be1b7408 = (byte *)0x0;
                        DAT_ffff0000be1b7410 = 0;
                        return 0x7016;
                      }
                      lVar10 = lVar10 + 2;
                    } while ((ulong)uVar5 << 1 != lVar10);
                  }
                  uVar5 = DAT_ffff0000be1b7618 + 1 >> 1;
                  if (uVar5 != DAT_ffff0000be1b7824) {
                    DAT_ffff0000be1b7408 = (byte *)0x0;
                    DAT_ffff0000be1b7410 = 0;
                    return 0x7017;
                  }
                  if (1 < DAT_ffff0000be1b7618 + 1) {
                    lVar10 = 0;
                    do {
                      if ((DAT_ffff0000be1b7618 & 1) == 0) {
                        uVar9 = *(ushort *)(&DAT_ffff0000be1b7418 + lVar10) >> 8 |
                                *(ushort *)(&DAT_ffff0000be1b7418 + lVar10) << 8;
                      }
                      else {
                        uVar9 = (ushort)DAT_ffff0000be1b7418;
                        if (lVar10 != 0) {
                          uVar9 = CONCAT11((&DAT_ffff0000be1b7418)[(int)lVar10 - 1],
                                           (&DAT_ffff0000be1b7418)[lVar10]);
                        }
                      }
                      if (*(ushort *)((long)&DAT_ffff0000be1b792c + lVar10) != uVar9) {
                        DAT_ffff0000be1b7408 = (byte *)0x0;
                        DAT_ffff0000be1b7410 = 0;
                        return 0x7018;
                      }
                      lVar10 = lVar10 + 2;
                    } while ((ulong)uVar5 << 1 != lVar10);
                  }
                  uVar7 = FUN_ffff0000be089d90(&DAT_ffff0000be1b7820,uVar12,&DAT_ffff0000be1b7418);
                }
                uVar12 = DAT_ffff0000be1b7618;
                if ((int)uVar7 != 0) {
                  return uVar7;
                }
                iVar6 = FUN_ffff0000be07f244(DAT_ffff0000be1b7618);
                if (iVar6 != 0) {
                  FUN_ffff0000be060ae0(&DAT_ffff0000be1b7180,0,0x200);
                  FUN_ffff0000be087428(&DAT_ffff0000be1b7180,&DAT_ffff0000be1b7418,uVar12);
                  DAT_ffff0000be1b7380 = uVar12;
                  if (1 < uVar12) {
                    uVar7 = (ulong)(uVar12 >> 1);
                    puVar11 = &DAT_ffff0000be1b7180;
                    do {
                      uVar12 = uVar12 - 1;
                      uVar7 = uVar7 - 1;
                      uVar1 = *puVar11;
                      *puVar11 = (&DAT_ffff0000be1b7180)[uVar12];
                      (&DAT_ffff0000be1b7180)[uVar12] = uVar1;
                      puVar11 = puVar11 + 1;
                    } while (uVar7 != 0);
                  }
                  DAT_ffff0000be1b7160 = (byte *)(param_1 + (ulong)*(uint *)(param_1 + 0x34));
                  DAT_ffff0000be1b7170 = (ulong)(param_2 - *(uint *)(param_1 + 0x34));
                  uVar7 = FUN_ffff0000be08c360();
                  if ((int)uVar7 != 0) {
                    return uVar7;
                  }
                  DAT_ffff0000be1b7170 = (ulong)DAT_ffff0000be1b7178;
                  DAT_ffff0000be1b7160 = (byte *)DAT_ffff0000be1b7168;
                  uVar7 = FUN_ffff0000be08a354(param_1,param_2,0xf);
                  if ((int)uVar7 != 0) {
                    return uVar7;
                  }
                  if (DAT_ffff0000be0f40a8 == 0x2000001) {
                    uVar7 = FUN_ffff0000be08a354(param_1,param_2,0x10);
                    return uVar7;
                  }
                  return uVar7;
                }
                uVar12 = 0x1026;
                goto LAB_ffff0000be08bf0c;
              }
            }
            DAT_ffff0000be1b7408 = DAT_ffff0000be1b7408 + uVar7;
            if (DAT_ffff0000be1b7410 == 0) {
              DAT_ffff0000be1b7408 = (byte *)0x0;
              DAT_ffff0000be1b7410 = 0;
              return 0x7014;
            }
          }
          FUN_ffff0000be05fd98
                    (s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        }
      }
    }
    DAT_ffff0000be1b7408 = (byte *)0x0;
    DAT_ffff0000be1b7410 = 0;
    return 0x7014;
  }
  uVar12 = *(uint *)(param_1 + 0x34);
  local_68 = (byte *)0x0;
  local_6c = 0;
  DAT_ffff0000be1b7160 = (byte *)(param_1 + (ulong)uVar12);
  DAT_ffff0000be1b7170 = (ulong)(param_2 - uVar12);
  if ((uVar12 <= param_2 && param_2 - uVar12 != 0) &&
     (uVar5 = FUN_ffff0000be08a7c0(&local_68,&local_6c), uVar5 == 0)) {
    DAT_ffff0000be1b7170 = (ulong)local_6c;
    DAT_ffff0000be1b7160 = local_68;
    uVar5 = FUN_ffff0000be08af08();
    if (uVar5 == 0) {
      uVar8 = *(undefined4 *)(puVar3 + 0x410);
      goto LAB_ffff0000be08bf38;
    }
  }
  return (ulong)uVar5;
}


