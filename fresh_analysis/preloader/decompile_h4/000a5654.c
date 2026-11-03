// File offset entry: 0xa5654

/* WARNING: Possible PIC construction at 0x020a5634: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a56f0: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a573c: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a57ac: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a5804: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a57b0) */
/* WARNING: Removing unreachable block (ram,0x020a57b8) */
/* WARNING: Removing unreachable block (ram,0x020a57f4) */
/* WARNING: Removing unreachable block (ram,0x020a5740) */
/* WARNING: Removing unreachable block (ram,0x020a5748) */
/* WARNING: Removing unreachable block (ram,0x020a5754) */
/* WARNING: Removing unreachable block (ram,0x020a5768) */
/* WARNING: Removing unreachable block (ram,0x020a5774) */
/* WARNING: Removing unreachable block (ram,0x020a579c) */
/* WARNING: Removing unreachable block (ram,0x020a56f4) */
/* WARNING: Removing unreachable block (ram,0x020a56fc) */
/* WARNING: Removing unreachable block (ram,0x020a5638) */
/* WARNING: Removing unreachable block (ram,0x020a5640) */
/* WARNING: Removing unreachable block (ram,0x020a5704) */
/* WARNING: Removing unreachable block (ram,0x020a5688) */
/* WARNING: Removing unreachable block (ram,0x020a570c) */
/* WARNING: Removing unreachable block (ram,0x020a5710) */
/* WARNING: Removing unreachable block (ram,0x020a56dc) */
/* WARNING: Removing unreachable block (ram,0x020a5808) */
/* WARNING: Removing unreachable block (ram,0x020a5810) */
/* WARNING: Removing unreachable block (ram,0x020a5824) */
/* WARNING: Removing unreachable block (ram,0x020a5958) */
/* WARNING: Removing unreachable block (ram,0x020a5974) */
/* WARNING: Removing unreachable block (ram,0x020a5a24) */
/* WARNING: Removing unreachable block (ram,0x020a58d8) */

ulong FUN_020a5554(int *param_1,int param_2,long param_3)

{
  uint uVar1;
  uint uVar2;
  uint *puVar3;
  bool bVar4;
  uint uVar5;
  int iVar6;
  ulong uVar7;
  ulong uVar8;
  byte *pbVar9;
  undefined8 uStack_c8;
  undefined4 uStack_c0;
  undefined4 uStack_bc;
  long lStack_b8;
  undefined1 *puStack_b0;
  undefined8 uStack_a8;
  
  func_0x02091664(0x21240ec,0,0x408);
  uRam00000000021242ec = *puRam0000000002103838;
  uRam00000000021244f0 = uRam00000000021242ec;
  if ((*param_1 != 0x58881688) || (param_1[0xc] != 0x58891689)) {
    return 0x101018;
  }
  iRam0000000002100ee4 = param_1[0xf];
  if (iRam0000000002100ee4 + 0xfdfffffeU < 0xfffffffe) {
    return 0x101019;
  }
  lRam0000000002123840 = (long)param_1 + (ulong)(uint)param_1[0xd];
  iRam0000000002123850 = param_2 - param_1[0xd];
  uStack_a8 = 0x20a5638;
  lStack_b8 = *plRam0000000002103280;
  uStack_bc = 0;
  uStack_c8 = 0;
  lRam0000000002123ae0 = lRam0000000002123840;
  iRam0000000002123ae8 = iRam0000000002123850;
  if (iRam0000000002123850 == 0) {
    uVar7 = 0;
    bVar4 = false;
    puStack_b0 = &stack0xffffffffffffffb0;
  }
  else {
    puStack_b0 = &stack0xffffffffffffffb0;
    uVar7 = func_0x020a6a40(0xc);
    if ((int)uVar7 == 0) {
      while( true ) {
        uStack_c0 = 0;
        uVar7 = func_0x020a6c9c(2,&uStack_c8,&uStack_bc,&uStack_c0,0);
        if ((int)uVar7 != 0) break;
        uVar5 = func_0x020a6e7c(uStack_c8,(byte *)(param_3 + 0x408),4,(uint *)(param_3 + 0x40c));
        bVar4 = uVar5 == 0;
        uVar7 = (ulong)uVar5;
        if ((uVar5 == 0) || (iRam0000000002123ae8 == 0)) goto LAB_020a5a38;
        uVar7 = func_0x020a6a40(0xc);
        if ((int)uVar7 != 0) goto LAB_020a5a34;
      }
      goto LAB_020a5b28;
    }
    bVar4 = false;
  }
LAB_020a58e0:
  if (iRam0000000002100edc == -1) {
    iRam0000000002100edc = 0;
    uVar7 = 0;
    *puRam0000000002103810 = 0;
  }
  else {
    if ((iRam0000000002100edc == 0) && (iRam0000000002100ee0 == -1)) {
      uVar7 = 0;
    }
    else {
      if ((iRam0000000002100edc != 1) || (iRam0000000002100ee0 != -1)) {
        uVar7 = uVar7 & 0xffffffff;
        goto LAB_020a5a44;
      }
      uVar7 = 0x10101c;
    }
    iRam0000000002100edc = -1;
    iRam0000000002100ee0 = -1;
  }
  goto LAB_020a5a44;
LAB_020a5a34:
  bVar4 = false;
LAB_020a5a38:
  if (uVar5 != 0) goto LAB_020a58e0;
LAB_020a5a44:
  puVar3 = puRam0000000002103850;
  if (bVar4) {
    uVar5 = *(uint *)(param_3 + 0x40c);
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
      pbVar9 = (byte *)(param_3 + 0x408);
      do {
        uVar8 = uVar8 - 1;
        uVar5 = (uint)*pbVar9 | uVar5 << 8;
        pbVar9 = pbVar9 + 1;
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
  lRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
LAB_020a5b28:
  if (*plRam0000000002103280 == lStack_b8) {
    return uVar7;
  }
  func_0x02091090(uVar7);
  lRam0000000002123ae0 = lRam0000000002123840;
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
  lRam0000000002123ae0 = 0;
  iRam0000000002123ae8 = 0;
  return (ulong)uVar5;
}


