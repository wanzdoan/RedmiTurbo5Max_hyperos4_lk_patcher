// File offset entry: 0xa876c

/* WARNING: Possible PIC construction at 0x020a86f0: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8764: Changing call to branch */
/* WARNING: Possible PIC construction at 0x020a8798: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x020a8768) */
/* WARNING: Removing unreachable block (ram,0x020a8774) */
/* WARNING: Removing unreachable block (ram,0x020a87a4) */
/* WARNING: Removing unreachable block (ram,0x020a87c0) */
/* WARNING: Removing unreachable block (ram,0x020a8780) */
/* WARNING: Removing unreachable block (ram,0x020a87c4) */
/* WARNING: Removing unreachable block (ram,0x020a86f4) */
/* WARNING: Removing unreachable block (ram,0x020a8708) */
/* WARNING: Removing unreachable block (ram,0x020a8788) */
/* WARNING: Removing unreachable block (ram,0x020a8710) */
/* WARNING: Removing unreachable block (ram,0x020a879c) */
/* WARNING: Removing unreachable block (ram,0x020a8700) */
/* WARNING: Removing unreachable block (ram,0x020a8714) */
/* WARNING: Removing unreachable block (ram,0x020a8728) */
/* WARNING: Removing unreachable block (ram,0x020a87d0) */
/* WARNING: Removing unreachable block (ram,0x020a8730) */
/* WARNING: Removing unreachable block (ram,0x020a8720) */

undefined8 FUN_020a866c(long param_1,byte param_2)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  long *plVar4;
  uint uVar5;
  long lStack_50;
  long lStack_48;
  undefined8 uStack_40;
  undefined8 uStack_38;
  
  bVar1 = param_2 == 3;
  uStack_38 = *puRam0000000002103280;
  if (param_2 < 4) {
    lStack_48 = 0;
    uStack_40 = 0;
    lStack_50 = 0;
    FUN_02091664(&lStack_50,0,0x18);
    plVar4 = &lStack_50;
    lStack_50 = param_1;
    lStack_48 = (ulong)CONCAT61(lStack_48._2_6_,param_2) << 8;
  }
  else {
    uVar3 = FUN_020a9218(0x7246);
    if (bVar1) {
      return uVar3;
    }
    plVar4 = (long *)FUN_02091090();
  }
  if (plVar4 == (long *)0x0) {
    uVar3 = 0x7247;
  }
  else {
    uVar5 = (*(byte *)((long)plVar4 + 9) & 3) << 4 | (*(byte *)(plVar4 + 1) & 1) << 1 | 1;
    if (*(byte *)(plVar4 + 1) != 0) {
      uVar5 = (*(byte *)((long)plVar4 + 10) & 1) << 8 | (*(byte *)((long)plVar4 + 0xb) & 1) << 9 |
              (*(uint *)((long)plVar4 + 0xc) & 3) << 10 | uVar5;
    }
    *(uint *)(*plVar4 + 0xd0) = uVar5;
    iVar2 = FUN_020a9354(*plVar4 + 0xd0,1,1,5000);
    if (iVar2 == 0) {
      uVar5 = *(uint *)(*plVar4 + 0xd0);
      if ((uVar5 >> 0x1c & 1) == 0) {
        if ((char)plVar4[1] == '\0') {
          uVar3 = 0;
          *(byte *)((long)plVar4 + 10) = (byte)(uVar5 >> 0x10) & 1;
          *(byte *)((long)plVar4 + 0xb) = (byte)(uVar5 >> 0x11) & 1;
          *(byte *)(plVar4 + 2) = (byte)(uVar5 >> 0x14) & 1;
          *(uint *)((long)plVar4 + 0xc) = uVar5 >> 0x12 & 3;
        }
        else {
          uVar3 = 0;
        }
      }
      else {
        uVar3 = 0x72ce;
      }
    }
    else {
      uVar3 = 0x7262;
    }
  }
  return uVar3;
}


