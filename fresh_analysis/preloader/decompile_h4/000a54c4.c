// File offset entry: 0xa54c4

int FUN_020a53c4(undefined8 param_1,ulong param_2)

{
  int iVar1;
  undefined4 uVar2;
  undefined8 *puVar3;
  undefined4 *puVar4;
  int iVar5;
  uint uVar6;
  undefined8 uVar7;
  undefined8 uStack_78;
  undefined8 uStack_70;
  undefined8 uStack_68;
  undefined8 uStack_60;
  undefined8 uStack_58;
  undefined8 uStack_50;
  undefined8 uStack_48;
  undefined8 uStack_40;
  long lStack_38;
  
  uVar2 = uRam0000000002100ed8;
  lStack_38 = *plRam0000000002103280;
  uStack_48 = 0;
  uStack_40 = 0;
  uStack_58 = 0;
  uStack_50 = 0;
  uStack_68 = 0;
  uStack_60 = 0;
  uStack_78 = 0;
  uStack_70 = 0;
  iVar5 = FUN_020a4c6c(param_1,param_2,iRam0000000002123194 - (int)param_2,&uStack_78,
                       uRam0000000002100ed8);
  if (iVar5 == 0) {
    iVar5 = func_0x020a0e58(lRam0000000002103800 + 8,&uStack_78,uVar2);
    if (iVar5 == 0) {
      iVar5 = func_0x020a7ce0(param_1,param_2 & 0xffffffff);
      if (iVar5 == 0) {
        func_0x0208ada0(0x20caa63,0x20c9773);
        goto LAB_020a5480;
      }
    }
    else {
      iVar5 = 0x101004;
    }
  }
  func_0x0208ada0(0x20b6160,0x20c9773,iVar5);
LAB_020a5480:
  if (*plRam0000000002103280 == lStack_38) {
    return iVar5;
  }
  uVar6 = func_0x02091090();
  *puRam0000000002103810 = uVar6;
  if (uVar6 < 3) {
    iVar1 = uVar6 * 0x80 + 0x100;
    uVar2 = *(undefined4 *)((long)(int)uVar6 * 4 + 0x20e41b8);
    iVar5 = 0;
    uVar7 = *(undefined8 *)((long)(int)uVar6 * 8 + 0x2100ee8);
    *piRam0000000002103838 = iVar1;
    puVar4 = puRam0000000002103828;
    puVar3 = puRam0000000002103820;
    *piRam0000000002103818 = iVar1;
    *puVar4 = uVar2;
    *puVar3 = uVar7;
  }
  else {
    iVar5 = 0x10101b;
  }
  return iVar5;
}


