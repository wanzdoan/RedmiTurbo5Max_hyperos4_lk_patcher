// File offset entry: 0xa5950

/* WARNING: Removing unreachable block (ram,0x020a58d8) */

ulong FUN_020a5850(byte *param_1,undefined4 param_2,uint *param_3,uint param_4)

{
  uint uVar1;
  uint uVar2;
  uint *puVar3;
  bool bVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  ulong uVar8;
  undefined8 uStack_78;
  undefined4 uStack_70;
  undefined4 uStack_6c;
  long lStack_68;
  
  lStack_68 = *plRam0000000002103280;
  uStack_6c = 0;
  uStack_78 = 0;
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 == 0) {
    uVar7 = 0;
    bVar4 = false;
  }
  else {
    uVar7 = func_0x020a6a40(param_4);
    if ((int)uVar7 == 0) {
      do {
        uStack_70 = 0;
        if ((param_4 < 0x13) && ((1 << (ulong)(param_4 & 0x1f) & 0x78430U) != 0)) {
          uVar7 = func_0x020a6c9c(3,&uStack_78,&uStack_6c,&uStack_70,0);
          if ((int)uVar7 != 0) goto LAB_020a5b28;
          uVar5 = func_0x020a6de8(uStack_78,param_1,param_2,param_3);
        }
        else {
          uVar7 = func_0x020a6c9c(2,&uStack_78,&uStack_6c,&uStack_70,0);
          if ((int)uVar7 != 0) goto LAB_020a5b28;
          uVar5 = func_0x020a6e7c(uStack_78,param_1,param_2,param_3);
        }
        bVar4 = uVar5 == 0;
        uVar7 = (ulong)uVar5;
        if ((uVar5 == 0) || (iRam0000000002123ae8 == 0)) goto LAB_020a5a38;
        uVar7 = func_0x020a6a40(param_4);
        if ((int)uVar7 != 0) goto LAB_020a5a34;
      } while( true );
    }
    bVar4 = false;
  }
LAB_020a58e0:
  if ((param_4 == 0xc) && (iRam0000000002100edc == -1)) {
    iRam0000000002100edc = 0;
    uVar7 = 0;
    *puRam0000000002103810 = 0;
  }
  else {
    if ((param_4 == 0xc) && ((iRam0000000002100edc == 0 && (iRam0000000002100ee0 == -1)))) {
      uVar7 = 0;
    }
    else {
      if ((param_4 != 0xc) || ((iRam0000000002100edc != 1 || (iRam0000000002100ee0 != -1)))) {
        uVar5 = (uint)uVar7;
        if (param_4 != 0xc) {
          uVar5 = 0x101011;
        }
        uVar7 = (ulong)uVar5;
        goto LAB_020a5a44;
      }
      uVar7 = 0x10101c;
    }
    iRam0000000002100edc = -1;
    iRam0000000002100ee0 = -1;
  }
LAB_020a5a44:
  puVar3 = puRam0000000002103850;
  if ((param_4 == 0xc) && (bVar4)) {
    uVar5 = *param_3;
    uVar8 = (ulong)uVar5;
    if (4 < uVar5) {
      uVar7 = 0x10101d;
      goto LAB_020a5b28;
    }
    if (uVar5 == 0) {
      uVar5 = 0;
    }
    else {
      uVar5 = 0;
      do {
        uVar8 = uVar8 - 1;
        uVar5 = (uint)*param_1 | uVar5 << 8;
        param_1 = param_1 + 1;
      } while (uVar8 != 0);
    }
    *puRam0000000002103848 = uVar5 >> 0x10 & 0xf;
    *puVar3 = uVar5 >> 0x14 & 0xf;
    if (iRam0000000002100edc == -1) {
      iRam0000000002100edc = 1;
      *puRam0000000002103810 = uVar5;
    }
    else {
      uVar1 = uVar5 & 0xf;
      uVar2 = 0;
      if (iRam0000000002100edc != 0 || iRam0000000002100ee0 != -1) {
        uVar1 = *puRam0000000002103810;
        uVar2 = uVar5 & 0xf;
      }
      uVar5 = (uint)uVar7;
      if (uVar1 != uVar2) {
        uVar5 = 0x10101c;
      }
      uVar7 = (ulong)uVar5;
      iRam0000000002100edc = -1;
      iRam0000000002100ee0 = -1;
    }
  }
  uRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
LAB_020a5b28:
  if (*plRam0000000002103280 == lStack_68) {
    return uVar7;
  }
  FUN_02091090(uVar7);
  uRam0000000002123ae0 = uRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  uVar5 = 0x101012;
  if (iRam0000000002123850 != 0) {
    do {
      iVar6 = func_0x020a6fa8(0x21240ec);
      if (iVar6 == 0) break;
    } while (iRam0000000002123ae8 != 0);
    uVar5 = 0;
    if (iVar6 != 0) {
      uVar5 = 0x101012;
    }
  }
  uRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
  return (ulong)uVar5;
LAB_020a5a34:
  bVar4 = false;
LAB_020a5a38:
  if (uVar5 != 0) goto LAB_020a58e0;
  goto LAB_020a5a44;
}


