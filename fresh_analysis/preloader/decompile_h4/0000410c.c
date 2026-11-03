// File offset entry: 0x410c

/* WARNING: Type propagation algorithm not settling */

ulong FUN_0200400c(byte *param_1,uint param_2)

{
  ushort uVar1;
  undefined1 in_ZR;
  undefined1 uVar2;
  byte bVar3;
  int iVar4;
  undefined4 uVar5;
  uint uVar6;
  uint uVar7;
  long lVar8;
  undefined8 uVar9;
  undefined8 *puVar10;
  uint *puVar12;
  ulong uVar13;
  byte *pbVar14;
  ushort *puVar15;
  byte *unaff_x19;
  undefined8 unaff_x20;
  ulong unaff_x21;
  ulong unaff_x22;
  undefined8 uStack_248;
  undefined8 uStack_240;
  undefined8 uStack_238;
  undefined8 uStack_230;
  undefined8 uStack_228;
  undefined1 ******ppppppuStack_220;
  undefined8 uStack_218;
  undefined8 *puStack_210;
  ulong uStack_208;
  undefined8 uStack_1f8;
  undefined8 uStack_1f0;
  undefined8 uStack_1e8;
  undefined8 uStack_1e0;
  undefined8 uStack_1d8;
  undefined1 *****pppppuStack_1d0;
  undefined8 uStack_1c8;
  undefined8 *puStack_1c0;
  ulong uStack_1b8;
  undefined8 uStack_1a8;
  ulong uStack_1a0;
  undefined8 uStack_198;
  undefined8 uStack_190;
  undefined8 uStack_188;
  undefined1 ****ppppuStack_180;
  undefined8 uStack_178;
  undefined8 uStack_168;
  undefined8 uStack_160;
  undefined8 uStack_158;
  undefined8 uStack_150;
  undefined8 uStack_148;
  undefined1 ***pppuStack_140;
  undefined8 uStack_138;
  undefined8 *puStack_130;
  ulong uStack_128;
  undefined8 uStack_118;
  undefined8 uStack_110;
  undefined8 uStack_108;
  undefined8 uStack_100;
  undefined8 uStack_f8;
  undefined1 **ppuStack_f0;
  undefined8 uStack_e8;
  undefined8 *puStack_e0;
  ulong uStack_d8;
  undefined8 uStack_c8;
  undefined8 uStack_c0;
  undefined8 uStack_b8;
  undefined8 uStack_b0;
  undefined8 uStack_a8;
  undefined1 *puStack_a0;
  undefined8 uStack_98;
  ulong uStack_90;
  ulong uStack_88;
  undefined8 uStack_80;
  byte *pbStack_78;
  byte abStack_68 [32];
  undefined8 uStack_48;
  ulong uVar11;
  
  uStack_48 = uRam0000000002100880;
  if (param_1 == (byte *)0x0) {
    uVar9 = 0x20bf43f;
LAB_020041e4:
    FUN_0208ada0(uVar9);
    param_1 = unaff_x19;
LAB_020041e8:
    uVar9 = 0xffffffff;
  }
  else {
    unaff_x20 = 0x20e5000;
    unaff_x22 = (ulong)param_2;
    uVar13 = (ulong)uRam00000000020e51e8;
    if ((int)uRam00000000020e51e8 < 0) {
      iVar4 = FUN_0207e344(0x20cbe9f);
      uVar13 = (ulong)(iVar4 == 0);
      uRam00000000020e51e8 = (uint)(iVar4 == 0);
    }
    unaff_x21 = *(ulong *)(uVar13 * 8 + 0x20e51f0);
    if (param_2 == 0) {
      unaff_x22 = 0x42414342;
      in_ZR = iRam000000000210e00c == 0x42414342;
      if (!(bool)in_ZR) {
        unaff_x20 = 0x210e008;
        lVar8 = FUN_0207e78c(unaff_x21,0x800,0x210e008,0x20);
        uVar7 = uRam000000000210e024;
        in_ZR = lVar8 == 0;
        if (lVar8 < 1) {
          FUN_0208ada0(0x20c25a3,0x20c0607);
          goto LAB_020041e8;
        }
        in_ZR = iRam000000000210e00c == 0x42414342;
        if ((bool)in_ZR) {
          unaff_x21 = (ulong)uRam000000000210e024;
          uVar6 = func_0x02004b10(0x210e008);
          in_ZR = 1;
          if (uVar7 == uVar6) goto LAB_02004408;
          unaff_x21 = 0x210e008;
          iVar4 = FUN_020915d8(0x210e008,0x20d1708,4);
          uVar7 = uRam000000000210e024;
          if (iVar4 == 0) {
            func_0x02004b10(0x210e008);
            uVar6 = func_0x02003498();
            if (uVar7 == uVar6) {
              uVar9 = 0x20b0069;
              in_ZR = 1;
              goto LAB_020043dc;
            }
          }
          uVar9 = 0x20c9af5;
LAB_0200431c:
          FUN_0208ada0(uVar9);
          uVar13 = FUN_02091524(0x20b313c);
          if (uVar13 < 4) {
            unaff_x21 = FUN_02091524(0x20b313c);
          }
          else {
            unaff_x21 = 2;
          }
          FUN_020915ac(0x210e008,0x20b313c,unaff_x21);
          *(undefined1 *)(unaff_x21 + 0x210e008) = 0;
          iRam000000000210e00c = 0x42414342;
          uRam000000000210e010 = 1;
          bRam000000000210e011 = bRam000000000210e011 & 0xf8 | 2;
          lVar8 = 0;
          while (in_ZR = lVar8 == 4, !(bool)in_ZR) {
            uVar2 = 0x7f;
            if (lVar8 == 0) {
              uVar2 = 0xff;
            }
            *(undefined1 *)(lVar8 + 0x210e014) = uVar2;
            lVar8 = lVar8 + 2;
          }
          uRam000000000210e024 = func_0x02004b10(0x210e008);
          iVar4 = func_0x02004aac(1);
          if (iVar4 != 0) {
            FUN_0208ada0(0x20b52cc);
          }
        }
        else {
          iVar4 = FUN_020915d8(0x210e008,0x20d1708,4);
          if (iVar4 != 0) {
            uVar9 = 0x20b4101;
            goto LAB_0200431c;
          }
          uVar9 = 0x20c9b23;
LAB_020043dc:
          FUN_0208ada0(uVar9);
          func_0x02004b88();
        }
        FUN_0200400c(0x210e008,1);
        FUN_0208ada0(0x20bd381,0x20c0607);
      }
LAB_02004408:
      FUN_020915ac(param_1,0x210e008,0x20);
    }
    else {
      in_ZR = param_2 == 2;
      if (2 < param_2) {
        uVar9 = 0x20caca0;
        unaff_x19 = param_1;
        goto LAB_020041e4;
      }
      in_ZR = ((uint)param_1[6] << 0x10 | (uint)param_1[7] << 0x18 | (uint)*(ushort *)(param_1 + 4))
              == 0x42414342;
      if ((bool)in_ZR) {
        uVar5 = func_0x02004b10(param_1);
        unaff_x20 = 0x210e008;
        param_1[0x1c] = (byte)uVar5;
        param_1[0x1f] = (byte)((uint)uVar5 >> 0x18);
        param_1[0x1e] = (byte)((uint)uVar5 >> 0x10);
        param_1[0x1d] = (byte)((uint)uVar5 >> 8);
        FUN_020915ac(0x210e008,param_1,0x20);
        if (param_2 == 2 && param_1[0x14] == 1) {
          abStack_68[0x10] = 0;
          abStack_68[0x11] = 0;
          abStack_68[0x12] = 0;
          abStack_68[0x13] = 0;
          abStack_68[0x14] = 0;
          abStack_68[0x15] = 0;
          abStack_68[0x16] = 0;
          abStack_68[0x17] = 0;
          abStack_68[0x18] = 0;
          abStack_68[0x19] = 0;
          abStack_68[0x1a] = 0;
          abStack_68[0x1b] = 0;
          abStack_68[0x1c] = 0;
          abStack_68[0x1d] = 0;
          abStack_68[0x1e] = 0;
          abStack_68[0x1f] = 0;
          abStack_68[0] = 0;
          abStack_68[1] = 0;
          abStack_68[2] = 0;
          abStack_68[3] = 0;
          abStack_68[4] = 0;
          abStack_68[5] = 0;
          abStack_68[6] = 0;
          abStack_68[7] = 0;
          abStack_68[8] = 0;
          abStack_68[9] = 0;
          abStack_68[10] = 0;
          abStack_68[0xb] = 0;
          abStack_68[0xc] = 0;
          abStack_68[0xd] = 0;
          abStack_68[0xe] = 0;
          abStack_68[0xf] = 0;
          FUN_0208ada0(0x20b00a8,0x20c0607);
          param_1 = abStack_68;
          FUN_020915ac(abStack_68,0x210e008,0x20);
          FUN_02091664(0x210e008,0,0x20);
          FUN_020915ac(0x210e008,0x20d1708,4);
          iRam000000000210e00c = CONCAT22(iRam000000000210e00c._2_2_,1);
          pbVar14 = (byte *)0x210e012;
          for (lVar8 = 0xc; lVar8 != 0x10; lVar8 = lVar8 + 2) {
            bVar3 = param_1[lVar8];
            pbVar14[-2] = bVar3 & 0xf;
            *pbVar14 = bVar3 >> 7;
            pbVar14[-1] = bVar3 >> 4 & 7;
            pbVar14 = pbVar14 + 4;
          }
          func_0x02004b10(0x210e008);
          uRam000000000210e024 = func_0x02003498();
          lVar8 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
          in_ZR = lVar8 == 0;
          if (lVar8 < 1) {
            uVar9 = 0x20bb3e5;
            goto LAB_020041f8;
          }
        }
        else {
          lVar8 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
          in_ZR = lVar8 == 0;
          if (lVar8 < 1) {
            uVar9 = 0x20bd3a4;
            goto LAB_020041f8;
          }
        }
      }
      else {
        uVar9 = 0x20c6b2e;
LAB_020041f8:
        FUN_0208ada0(uVar9,0x20c0607);
      }
    }
    uVar9 = 0;
  }
  uVar13 = func_0x02004cf8(uVar9);
  if ((bool)in_ZR) {
    return uVar13;
  }
  puVar10 = (undefined8 *)FUN_02091090();
  uStack_98 = 0x2004448;
  uStack_a8 = *puRam0000000002103280;
  puStack_a0 = &stack0xffffffffffffffc0;
  uStack_90 = unaff_x22;
  uStack_88 = unaff_x21;
  uStack_80 = unaff_x20;
  pbStack_78 = param_1;
  uVar7 = func_0x02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar9 = 0x20bf41f;
LAB_020045b0:
    uVar13 = 0xffffffff;
    FUN_0208ada0(uVar9,0xffffffff);
  }
  else {
    if (puVar10 != (undefined8 *)0x0) {
      uStack_b8 = 0;
      uStack_b0 = 0;
      uStack_c8 = 0;
      uStack_c0 = 0;
      FUN_02091664(&uStack_c8,0,0x20);
      iVar4 = FUN_0200400c(&uStack_c8,0);
      if (iVar4 < 0) {
        uVar9 = 0x20c9ad6;
      }
      else {
        uVar13 = FUN_02091524(0x20b313c);
        if (uVar13 < 4) {
          lVar8 = FUN_02091524(0x20b313c);
        }
        else {
          lVar8 = 2;
        }
        uVar9 = 0x20b313c;
        if (uVar7 != 0) {
          uVar9 = 0x20cbdd2;
        }
        puVar10 = &uStack_c8;
        FUN_020915ac(&uStack_c8,uVar9,lVar8);
        *(undefined1 *)((long)puVar10 + lVar8) = 0;
        uVar13 = (long)&uStack_c0 + 4;
        pbVar14 = (byte *)(uVar13 | (ulong)(uVar7 == 0) << 1);
        *(byte *)(uVar13 + (ulong)uVar7 * 2) = *(byte *)(uVar13 + (ulong)uVar7 * 2) | 0xf;
        uVar2 = ((*pbVar14 ^ 0xff) & 0xf) == 0;
        uStack_c8 = CONCAT44(0x42414342,(undefined4)uStack_c8);
        if ((bool)uVar2) {
          pbVar14[1] = pbVar14[1];
          *pbVar14 = *pbVar14 & 0xf0 | 0xe;
        }
        iVar4 = FUN_0200400c(&uStack_c8,2);
        if (-1 < iVar4) {
          uVar13 = 0;
          goto LAB_020045b4;
        }
        uVar9 = 0x20b313f;
      }
      goto LAB_020045b0;
    }
    FUN_0208ada0(0x20c6b21);
    uVar13 = 0xffffffff;
  }
LAB_020045b4:
  func_0x02004cf8();
  if ((bool)uVar2) {
    return uVar13;
  }
  FUN_02091090();
  uStack_e8 = 0x20045dc;
  uStack_f8 = uRam0000000002100880;
  ppuStack_f0 = &puStack_a0;
  puStack_e0 = puVar10;
  uStack_d8 = uVar13;
  uVar7 = func_0x02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar9 = 0x20b40e9;
LAB_02004660:
    FUN_0208ada0(uVar9,0xffffffff);
    bVar3 = 0xff;
  }
  else {
    uVar13 = (ulong)uVar7;
    uStack_108 = 0;
    uStack_100 = 0;
    puVar10 = &uStack_118;
    uStack_118 = 0;
    uStack_110 = 0;
    FUN_02091664(&uStack_118,0,0x20);
    iVar4 = FUN_0200400c(&uStack_118,0);
    if (iVar4 < 0) {
      uVar9 = 0x20b8211;
      goto LAB_02004660;
    }
    bVar3 = *(byte *)((long)&uStack_110 + uVar13 * 2 + 4) >> 4 & 7;
  }
  uVar11 = func_0x02004cf8(bVar3);
  if ((bool)uVar2) {
    return uVar11;
  }
  FUN_02091090();
  uStack_138 = 0x200468c;
  uStack_148 = uRam0000000002100880;
  pppuStack_140 = &ppuStack_f0;
  puStack_130 = puVar10;
  uStack_128 = uVar13;
  uVar7 = func_0x02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar9 = 0x20b40e9;
LAB_02004764:
    uVar13 = 0xffffffff;
    FUN_0208ada0(uVar9,0xffffffff);
  }
  else {
    uStack_158 = 0;
    uStack_150 = 0;
    puVar10 = &uStack_168;
    uStack_168 = 0;
    uStack_160 = 0;
    FUN_02091664(&uStack_168,0,0x20);
    iVar4 = FUN_0200400c(&uStack_168,0);
    if (iVar4 < 0) {
      uVar9 = 0x20b8211;
      goto LAB_02004764;
    }
    puVar15 = (ushort *)((long)&uStack_160 + (ulong)uVar7 * 2 + 4);
    uVar1 = *puVar15;
    uVar2 = (uVar1 & 0x70) == 0;
    if (!(bool)uVar2) {
      *(char *)((long)&uStack_160 + (ulong)uVar7 * 2 + 5) = (char)(uVar1 >> 8);
      *(byte *)puVar15 = (byte)uVar1 & 0x8f | (byte)uVar1 + 0x70 & 0x70;
    }
    iVar4 = FUN_0200400c(&uStack_168,1);
    if (iVar4 < 0) {
      uVar9 = 0x20b313f;
      goto LAB_02004764;
    }
    uVar13 = 0;
  }
  func_0x02004cf8();
  if ((bool)uVar2) {
    return uVar13;
  }
  FUN_02091090();
  uStack_178 = 0x200478c;
  uStack_188 = *puRam0000000002103280;
  uStack_198 = 0;
  uStack_190 = 0;
  uStack_1a8 = 0;
  uStack_1a0 = 0;
  ppppuStack_180 = &pppuStack_140;
  FUN_02091664(&uStack_1a8,0,0x20);
  iVar4 = FUN_0200400c(&uStack_1a8,0);
  if (iVar4 < 0) {
    FUN_0208ada0(0x20b23a0,iVar4);
    iVar4 = -1;
  }
  else if ((uStack_1a0 & 0xf00000000) == 0) {
    uVar2 = (uStack_1a0 & 0xf000000000000) == 0;
    iVar4 = -(uint)(byte)uVar2;
  }
  else {
    iVar4 = 0;
    uVar2 = 0;
  }
  uVar11 = func_0x02004cf8(iVar4);
  if ((bool)uVar2) {
    return uVar11;
  }
  FUN_02091090();
  uStack_1c8 = 0x2004828;
  uStack_1d8 = uRam0000000002100880;
  pppppuStack_1d0 = &ppppuStack_180;
  puStack_1c0 = puVar10;
  uStack_1b8 = uVar13;
  uVar7 = func_0x02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar9 = 0x20cbe84;
LAB_020048e4:
    uVar13 = 0xffffffff;
    FUN_0208ada0(uVar9,0xffffffff);
  }
  else {
    uStack_1e8 = 0;
    uStack_1e0 = 0;
    puVar10 = &uStack_1f8;
    uStack_1f8 = 0;
    uStack_1f0 = 0;
    FUN_02091664(&uStack_1f8,0,0x20);
    iVar4 = FUN_0200400c(&uStack_1f8,0);
    if (iVar4 < 0) {
      uVar9 = 0x20bb3ca;
      goto LAB_020048e4;
    }
    pbVar14 = (byte *)((long)&uStack_1f0 + (ulong)uVar7 * 2 + 4);
    *pbVar14 = *pbVar14 & 0x70;
    iVar4 = FUN_0200400c(&uStack_1f8,1);
    if (iVar4 < 0) {
      uVar9 = 0x20ba5a3;
      goto LAB_020048e4;
    }
    uVar13 = 0;
  }
  func_0x02004cf8();
  if ((bool)uVar2) {
    return uVar13;
  }
  FUN_02091090();
  uStack_218 = 0x200490c;
  uStack_228 = uRam0000000002100880;
  ppppppuStack_220 = &pppppuStack_1d0;
  puStack_210 = puVar10;
  uStack_208 = uVar13;
  uVar7 = func_0x02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar9 = 0x20b133a;
  }
  else {
    uStack_238 = 0;
    uStack_230 = 0;
    uStack_248 = 0;
    uStack_240 = 0;
    FUN_02091664(&uStack_248,0,0x20);
    iVar4 = FUN_0200400c(&uStack_248,0);
    if (-1 < iVar4) {
      uVar13 = (ulong)(*(byte *)((long)&uStack_240 + (ulong)uVar7 * 2 + 4) >> 7);
      goto LAB_020049a0;
    }
    uVar9 = 0x20bb3ca;
  }
  uVar13 = 0xffffffff;
  FUN_0208ada0(uVar9,0xffffffff);
LAB_020049a0:
  func_0x02004cf8();
  if (!(bool)uVar2) {
    FUN_02091090();
    puVar12 = (uint *)FUN_020036c8(1);
    if (puVar12 == (uint *)0x0) {
      uVar13 = 0xffffffff;
    }
    else {
      uVar6 = *puVar12;
      uVar7 = uVar6;
      if (uVar6 != 2) {
        uVar7 = 0xffffffff;
      }
      if (uVar6 == 1) {
        uVar7 = 1;
      }
      uVar13 = (ulong)uVar7;
    }
    return uVar13;
  }
  return uVar13;
}


