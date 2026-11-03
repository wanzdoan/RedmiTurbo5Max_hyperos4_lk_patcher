// File offset entry: 0x4014

/* WARNING: Type propagation algorithm not settling */

ulong FUN_02003f14(void)

{
  ushort uVar1;
  undefined1 in_ZR;
  undefined1 uVar2;
  byte bVar3;
  int iVar4;
  undefined4 uVar5;
  uint uVar6;
  uint uVar7;
  undefined8 uVar8;
  ulong uVar9;
  long lVar10;
  undefined8 *puVar11;
  uint *puVar13;
  byte *pbVar14;
  byte *pbVar15;
  ushort *puVar16;
  byte *unaff_x19;
  undefined8 unaff_x20;
  ulong unaff_x21;
  ulong unaff_x22;
  undefined1 auVar17 [16];
  undefined8 uStack_298;
  undefined8 uStack_290;
  undefined8 uStack_288;
  undefined8 uStack_280;
  undefined8 uStack_278;
  undefined1 *******pppppppuStack_270;
  undefined8 uStack_268;
  undefined8 *puStack_260;
  ulong uStack_258;
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
  ulong uStack_1f0;
  undefined8 uStack_1e8;
  undefined8 uStack_1e0;
  undefined8 uStack_1d8;
  undefined1 *****pppppuStack_1d0;
  undefined8 uStack_1c8;
  undefined8 uStack_1b8;
  undefined8 uStack_1b0;
  undefined8 uStack_1a8;
  undefined8 uStack_1a0;
  undefined8 uStack_198;
  undefined1 ****ppppuStack_190;
  undefined8 uStack_188;
  undefined8 *puStack_180;
  ulong uStack_178;
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
  ulong uStack_e0;
  ulong uStack_d8;
  undefined8 uStack_d0;
  byte *pbStack_c8;
  byte abStack_b8 [32];
  undefined8 uStack_98;
  undefined1 *puStack_90;
  code *pcStack_88;
  undefined8 uStack_48;
  undefined8 uStack_40;
  undefined8 uStack_38;
  undefined8 uStack_30;
  undefined8 uStack_28;
  ulong uVar12;
  
  uStack_28 = *puRam0000000002103280;
  uStack_38 = 0;
  uStack_30 = 0;
  uStack_48 = 0;
  uStack_40 = 0;
  FUN_02091664(&uStack_48,0,0x20);
  iVar4 = FUN_0200400c(&uStack_48,0);
  if (iVar4 < 0) {
    FUN_0208ada0(0x20bf3dd,0xffffffff);
    uVar8 = 0;
  }
  else {
    in_ZR = uStack_48._4_4_ == 0x42414342;
    if ((bool)in_ZR) {
      in_ZR = (uStack_40._4_2_ & 0xf) == (uStack_40._6_2_ & 0xf);
      unaff_x19 = (byte *)(ulong)((uStack_40._4_2_ & 0xf) < (uStack_40._6_2_ & 0xf));
      FUN_0208ada0(0x20b2377);
      FUN_0208ada0(0x20bf3f9,uStack_40._4_2_ >> 4 & 7,uStack_40._6_2_ >> 4 & 7);
    }
    else {
      FUN_0208ada0(0x20b721c);
      unaff_x19 = (byte *)0x0;
    }
    uVar8 = *(undefined8 *)((long)unaff_x19 * 8 + 0x20e51d8);
  }
  uVar9 = FUN_02004cf8(uVar8);
  if ((bool)in_ZR) {
    return uVar9;
  }
  auVar17 = FUN_02091090();
  pbVar15 = auVar17._0_8_;
  pcStack_88 = FUN_0200400c;
  uStack_98 = uRam0000000002100880;
  puStack_90 = &stack0xffffffffffffffe0;
  if (pbVar15 == (byte *)0x0) {
    uVar8 = 0x20bf43f;
LAB_020041e4:
    FUN_0208ada0(uVar8);
    pbVar15 = unaff_x19;
LAB_020041e8:
    uVar8 = 0xffffffff;
  }
  else {
    unaff_x20 = 0x20e5000;
    unaff_x22 = auVar17._8_8_ & 0xffffffff;
    uVar9 = (ulong)uRam00000000020e51e8;
    if ((int)uRam00000000020e51e8 < 0) {
      iVar4 = FUN_0207e344(0x20cbe9f);
      uVar9 = (ulong)(iVar4 == 0);
      uRam00000000020e51e8 = (uint)(iVar4 == 0);
    }
    unaff_x21 = *(ulong *)(uVar9 * 8 + 0x20e51f0);
    uVar7 = auVar17._8_4_;
    if (uVar7 == 0) {
      unaff_x22 = 0x42414342;
      in_ZR = iRam000000000210e00c == 0x42414342;
      if (!(bool)in_ZR) {
        unaff_x20 = 0x210e008;
        lVar10 = FUN_0207e78c(unaff_x21,0x800,0x210e008,0x20);
        uVar7 = uRam000000000210e024;
        in_ZR = lVar10 == 0;
        if (lVar10 < 1) {
          FUN_0208ada0(0x20c25a3,0x20c0607);
          goto LAB_020041e8;
        }
        in_ZR = iRam000000000210e00c == 0x42414342;
        if ((bool)in_ZR) {
          unaff_x21 = (ulong)uRam000000000210e024;
          uVar6 = FUN_02004b10(0x210e008);
          in_ZR = 1;
          if (uVar7 == uVar6) goto LAB_02004408;
          unaff_x21 = 0x210e008;
          iVar4 = FUN_020915d8(0x210e008,0x20d1708,4);
          uVar7 = uRam000000000210e024;
          if (iVar4 == 0) {
            FUN_02004b10(0x210e008);
            uVar6 = FUN_02003498();
            if (uVar7 == uVar6) {
              uVar8 = 0x20b0069;
              in_ZR = 1;
              goto LAB_020043dc;
            }
          }
          uVar8 = 0x20c9af5;
LAB_0200431c:
          FUN_0208ada0(uVar8);
          uVar9 = FUN_02091524(0x20b313c);
          if (uVar9 < 4) {
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
          lVar10 = 0;
          while (in_ZR = lVar10 == 4, !(bool)in_ZR) {
            uVar2 = 0x7f;
            if (lVar10 == 0) {
              uVar2 = 0xff;
            }
            *(undefined1 *)(lVar10 + 0x210e014) = uVar2;
            lVar10 = lVar10 + 2;
          }
          uRam000000000210e024 = FUN_02004b10(0x210e008);
          iVar4 = FUN_02004aac(1);
          if (iVar4 != 0) {
            FUN_0208ada0(0x20b52cc);
          }
        }
        else {
          iVar4 = FUN_020915d8(0x210e008,0x20d1708,4);
          if (iVar4 != 0) {
            uVar8 = 0x20b4101;
            goto LAB_0200431c;
          }
          uVar8 = 0x20c9b23;
LAB_020043dc:
          FUN_0208ada0(uVar8);
          FUN_02004b88();
        }
        FUN_0200400c(0x210e008,1);
        FUN_0208ada0(0x20bd381,0x20c0607);
      }
LAB_02004408:
      FUN_020915ac(pbVar15,0x210e008,0x20);
    }
    else {
      in_ZR = uVar7 == 2;
      if (2 < uVar7) {
        uVar8 = 0x20caca0;
        unaff_x19 = pbVar15;
        goto LAB_020041e4;
      }
      in_ZR = ((uint)pbVar15[6] << 0x10 | (uint)pbVar15[7] << 0x18 | (uint)*(ushort *)(pbVar15 + 4))
              == 0x42414342;
      if ((bool)in_ZR) {
        uVar5 = FUN_02004b10(pbVar15);
        unaff_x20 = 0x210e008;
        pbVar15[0x1c] = (byte)uVar5;
        pbVar15[0x1f] = (byte)((uint)uVar5 >> 0x18);
        pbVar15[0x1e] = (byte)((uint)uVar5 >> 0x10);
        pbVar15[0x1d] = (byte)((uint)uVar5 >> 8);
        FUN_020915ac(0x210e008,pbVar15,0x20);
        if (uVar7 == 2 && pbVar15[0x14] == 1) {
          abStack_b8[0x10] = 0;
          abStack_b8[0x11] = 0;
          abStack_b8[0x12] = 0;
          abStack_b8[0x13] = 0;
          abStack_b8[0x14] = 0;
          abStack_b8[0x15] = 0;
          abStack_b8[0x16] = 0;
          abStack_b8[0x17] = 0;
          abStack_b8[0x18] = 0;
          abStack_b8[0x19] = 0;
          abStack_b8[0x1a] = 0;
          abStack_b8[0x1b] = 0;
          abStack_b8[0x1c] = 0;
          abStack_b8[0x1d] = 0;
          abStack_b8[0x1e] = 0;
          abStack_b8[0x1f] = 0;
          abStack_b8[0] = 0;
          abStack_b8[1] = 0;
          abStack_b8[2] = 0;
          abStack_b8[3] = 0;
          abStack_b8[4] = 0;
          abStack_b8[5] = 0;
          abStack_b8[6] = 0;
          abStack_b8[7] = 0;
          abStack_b8[8] = 0;
          abStack_b8[9] = 0;
          abStack_b8[10] = 0;
          abStack_b8[0xb] = 0;
          abStack_b8[0xc] = 0;
          abStack_b8[0xd] = 0;
          abStack_b8[0xe] = 0;
          abStack_b8[0xf] = 0;
          FUN_0208ada0(0x20b00a8,0x20c0607);
          pbVar15 = abStack_b8;
          FUN_020915ac(abStack_b8,0x210e008,0x20);
          FUN_02091664(0x210e008,0,0x20);
          FUN_020915ac(0x210e008,0x20d1708,4);
          iRam000000000210e00c = CONCAT22(iRam000000000210e00c._2_2_,1);
          pbVar14 = (byte *)0x210e012;
          for (lVar10 = 0xc; lVar10 != 0x10; lVar10 = lVar10 + 2) {
            bVar3 = pbVar15[lVar10];
            pbVar14[-2] = bVar3 & 0xf;
            *pbVar14 = bVar3 >> 7;
            pbVar14[-1] = bVar3 >> 4 & 7;
            pbVar14 = pbVar14 + 4;
          }
          FUN_02004b10(0x210e008);
          uRam000000000210e024 = FUN_02003498();
          lVar10 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
          in_ZR = lVar10 == 0;
          if (lVar10 < 1) {
            uVar8 = 0x20bb3e5;
            goto LAB_020041f8;
          }
        }
        else {
          lVar10 = FUN_0207e954(unaff_x21,0x800,0x210e008,0x20);
          in_ZR = lVar10 == 0;
          if (lVar10 < 1) {
            uVar8 = 0x20bd3a4;
            goto LAB_020041f8;
          }
        }
      }
      else {
        uVar8 = 0x20c6b2e;
LAB_020041f8:
        FUN_0208ada0(uVar8,0x20c0607);
      }
    }
    uVar8 = 0;
  }
  uVar9 = FUN_02004cf8(uVar8);
  if ((bool)in_ZR) {
    return uVar9;
  }
  puVar11 = (undefined8 *)FUN_02091090();
  uStack_e8 = 0x2004448;
  uStack_f8 = *puRam0000000002103280;
  ppuStack_f0 = &puStack_90;
  uStack_e0 = unaff_x22;
  uStack_d8 = unaff_x21;
  uStack_d0 = unaff_x20;
  pbStack_c8 = pbVar15;
  uVar7 = FUN_02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar8 = 0x20bf41f;
LAB_020045b0:
    uVar9 = 0xffffffff;
    FUN_0208ada0(uVar8,0xffffffff);
  }
  else {
    if (puVar11 != (undefined8 *)0x0) {
      uStack_108 = 0;
      uStack_100 = 0;
      uStack_118 = 0;
      uStack_110 = 0;
      FUN_02091664(&uStack_118,0,0x20);
      iVar4 = FUN_0200400c(&uStack_118,0);
      if (iVar4 < 0) {
        uVar8 = 0x20c9ad6;
      }
      else {
        uVar9 = FUN_02091524(0x20b313c);
        if (uVar9 < 4) {
          lVar10 = FUN_02091524(0x20b313c);
        }
        else {
          lVar10 = 2;
        }
        uVar8 = 0x20b313c;
        if (uVar7 != 0) {
          uVar8 = 0x20cbdd2;
        }
        puVar11 = &uStack_118;
        FUN_020915ac(&uStack_118,uVar8,lVar10);
        *(undefined1 *)((long)puVar11 + lVar10) = 0;
        uVar9 = (long)&uStack_110 + 4;
        pbVar15 = (byte *)(uVar9 | (ulong)(uVar7 == 0) << 1);
        *(byte *)(uVar9 + (ulong)uVar7 * 2) = *(byte *)(uVar9 + (ulong)uVar7 * 2) | 0xf;
        uVar2 = ((*pbVar15 ^ 0xff) & 0xf) == 0;
        uStack_118 = CONCAT44(0x42414342,(undefined4)uStack_118);
        if ((bool)uVar2) {
          pbVar15[1] = pbVar15[1];
          *pbVar15 = *pbVar15 & 0xf0 | 0xe;
        }
        iVar4 = FUN_0200400c(&uStack_118,2);
        if (-1 < iVar4) {
          uVar9 = 0;
          goto LAB_020045b4;
        }
        uVar8 = 0x20b313f;
      }
      goto LAB_020045b0;
    }
    FUN_0208ada0(0x20c6b21);
    uVar9 = 0xffffffff;
  }
LAB_020045b4:
  FUN_02004cf8();
  if ((bool)uVar2) {
    return uVar9;
  }
  FUN_02091090();
  uStack_138 = 0x20045dc;
  uStack_148 = uRam0000000002100880;
  pppuStack_140 = &ppuStack_f0;
  puStack_130 = puVar11;
  uStack_128 = uVar9;
  uVar7 = FUN_02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar8 = 0x20b40e9;
LAB_02004660:
    FUN_0208ada0(uVar8,0xffffffff);
    bVar3 = 0xff;
  }
  else {
    uVar9 = (ulong)uVar7;
    uStack_158 = 0;
    uStack_150 = 0;
    puVar11 = &uStack_168;
    uStack_168 = 0;
    uStack_160 = 0;
    FUN_02091664(&uStack_168,0,0x20);
    iVar4 = FUN_0200400c(&uStack_168,0);
    if (iVar4 < 0) {
      uVar8 = 0x20b8211;
      goto LAB_02004660;
    }
    bVar3 = *(byte *)((long)&uStack_160 + uVar9 * 2 + 4) >> 4 & 7;
  }
  uVar12 = FUN_02004cf8(bVar3);
  if ((bool)uVar2) {
    return uVar12;
  }
  FUN_02091090();
  uStack_188 = 0x200468c;
  uStack_198 = uRam0000000002100880;
  ppppuStack_190 = &pppuStack_140;
  puStack_180 = puVar11;
  uStack_178 = uVar9;
  uVar7 = FUN_02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar8 = 0x20b40e9;
LAB_02004764:
    uVar9 = 0xffffffff;
    FUN_0208ada0(uVar8,0xffffffff);
  }
  else {
    uStack_1a8 = 0;
    uStack_1a0 = 0;
    puVar11 = &uStack_1b8;
    uStack_1b8 = 0;
    uStack_1b0 = 0;
    FUN_02091664(&uStack_1b8,0,0x20);
    iVar4 = FUN_0200400c(&uStack_1b8,0);
    if (iVar4 < 0) {
      uVar8 = 0x20b8211;
      goto LAB_02004764;
    }
    puVar16 = (ushort *)((long)&uStack_1b0 + (ulong)uVar7 * 2 + 4);
    uVar1 = *puVar16;
    uVar2 = (uVar1 & 0x70) == 0;
    if (!(bool)uVar2) {
      *(char *)((long)&uStack_1b0 + (ulong)uVar7 * 2 + 5) = (char)(uVar1 >> 8);
      *(byte *)puVar16 = (byte)uVar1 & 0x8f | (byte)uVar1 + 0x70 & 0x70;
    }
    iVar4 = FUN_0200400c(&uStack_1b8,1);
    if (iVar4 < 0) {
      uVar8 = 0x20b313f;
      goto LAB_02004764;
    }
    uVar9 = 0;
  }
  FUN_02004cf8();
  if ((bool)uVar2) {
    return uVar9;
  }
  FUN_02091090();
  uStack_1c8 = 0x200478c;
  uStack_1d8 = *puRam0000000002103280;
  uStack_1e8 = 0;
  uStack_1e0 = 0;
  uStack_1f8 = 0;
  uStack_1f0 = 0;
  pppppuStack_1d0 = &ppppuStack_190;
  FUN_02091664(&uStack_1f8,0,0x20);
  iVar4 = FUN_0200400c(&uStack_1f8,0);
  if (iVar4 < 0) {
    FUN_0208ada0(0x20b23a0,iVar4);
    iVar4 = -1;
  }
  else if ((uStack_1f0 & 0xf00000000) == 0) {
    uVar2 = (uStack_1f0 & 0xf000000000000) == 0;
    iVar4 = -(uint)(byte)uVar2;
  }
  else {
    iVar4 = 0;
    uVar2 = 0;
  }
  uVar12 = FUN_02004cf8(iVar4);
  if ((bool)uVar2) {
    return uVar12;
  }
  FUN_02091090();
  uStack_218 = 0x2004828;
  uStack_228 = uRam0000000002100880;
  ppppppuStack_220 = &pppppuStack_1d0;
  puStack_210 = puVar11;
  uStack_208 = uVar9;
  uVar7 = FUN_02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar8 = 0x20cbe84;
LAB_020048e4:
    uVar9 = 0xffffffff;
    FUN_0208ada0(uVar8,0xffffffff);
  }
  else {
    uStack_238 = 0;
    uStack_230 = 0;
    puVar11 = &uStack_248;
    uStack_248 = 0;
    uStack_240 = 0;
    FUN_02091664(&uStack_248,0,0x20);
    iVar4 = FUN_0200400c(&uStack_248,0);
    if (iVar4 < 0) {
      uVar8 = 0x20bb3ca;
      goto LAB_020048e4;
    }
    pbVar15 = (byte *)((long)&uStack_240 + (ulong)uVar7 * 2 + 4);
    *pbVar15 = *pbVar15 & 0x70;
    iVar4 = FUN_0200400c(&uStack_248,1);
    if (iVar4 < 0) {
      uVar8 = 0x20ba5a3;
      goto LAB_020048e4;
    }
    uVar9 = 0;
  }
  FUN_02004cf8();
  if ((bool)uVar2) {
    return uVar9;
  }
  FUN_02091090();
  uStack_268 = 0x200490c;
  uStack_278 = uRam0000000002100880;
  pppppppuStack_270 = &ppppppuStack_220;
  puStack_260 = puVar11;
  uStack_258 = uVar9;
  uVar7 = FUN_02003eac();
  uVar2 = uVar7 == 0xffffffff;
  if ((bool)uVar2) {
    uVar8 = 0x20b133a;
  }
  else {
    uStack_288 = 0;
    uStack_280 = 0;
    uStack_298 = 0;
    uStack_290 = 0;
    FUN_02091664(&uStack_298,0,0x20);
    iVar4 = FUN_0200400c(&uStack_298,0);
    if (-1 < iVar4) {
      uVar9 = (ulong)(*(byte *)((long)&uStack_290 + (ulong)uVar7 * 2 + 4) >> 7);
      goto LAB_020049a0;
    }
    uVar8 = 0x20bb3ca;
  }
  uVar9 = 0xffffffff;
  FUN_0208ada0(uVar8,0xffffffff);
LAB_020049a0:
  FUN_02004cf8();
  if (!(bool)uVar2) {
    FUN_02091090();
    puVar13 = (uint *)FUN_020036c8(1);
    if (puVar13 == (uint *)0x0) {
      uVar9 = 0xffffffff;
    }
    else {
      uVar6 = *puVar13;
      uVar7 = uVar6;
      if (uVar6 != 2) {
        uVar7 = 0xffffffff;
      }
      if (uVar6 == 1) {
        uVar7 = 1;
      }
      uVar9 = (ulong)uVar7;
    }
    return uVar9;
  }
  return uVar9;
}


