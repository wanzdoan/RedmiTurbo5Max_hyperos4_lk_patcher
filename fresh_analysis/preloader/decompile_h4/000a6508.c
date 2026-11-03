// File offset entry: 0xa6508

int FUN_020a6408(int *param_1,int param_2,undefined8 param_3,long param_4)

{
  int iVar1;
  int iVar2;
  
  iVar1 = 0x101018;
  FUN_02091664(0x21240ec,0,0x408);
  uRam00000000021242ec = *puRam0000000002103838;
  uRam00000000021244f0 = uRam00000000021242ec;
  if ((*param_1 == 0x58881688) && (param_1[0xc] == 0x58891689)) {
    iRam0000000002100ee4 = param_1[0xf];
    lRam0000000002123840 = (long)param_1 + (ulong)(uint)param_1[0xd];
    iRam0000000002123850 = param_2 - param_1[0xd];
    if (iRam0000000002100ee4 == 0x2000002) {
      FUN_020a0fec();
      iVar1 = func_0x020a5850(param_4,4,param_4 + 4,0xc);
      if (iVar1 == 0) {
        iVar2 = func_0x020a5b68();
        if (((iVar2 == 0) && (iVar2 = func_0x020a6660(param_3), iVar2 == 0)) &&
           (iVar2 = func_0x020a6000(), iVar2 == 0)) {
          lRam0000000002123840 = lRam0000000002123848;
          iRam0000000002123850 = iRam0000000002123854;
        }
        iVar1 = func_0x020a5850(param_4 + 0x3c,0x30,param_4 + 0x6c,10);
        if (((iVar1 == 0) &&
            (iVar1 = func_0x020a5850(param_4 + 8,0x30,param_4 + 0x38,5), iVar1 == 0)) &&
           ((iVar1 = func_0x020a5850(param_4 + 0x70,4,param_4 + 0x74,6), iVar1 == 0 &&
            (iVar1 = func_0x020a5850(param_4 + 0x78,4,param_4 + 0x7c,9), iVar1 == 0)))) {
          FUN_0208ada0(0x20c4677,0x20c9773);
          func_0x020a5850(param_4 + 0xc4,0x20,param_4 + 0xe4,0xf);
          FUN_020a0fec(param_4 + 0xc4,*(undefined4 *)(param_4 + 0xe4));
          *(undefined4 *)(param_4 + 0x128) = 0x40;
          func_0x020a5850(param_4 + 0xe8,0x40,param_4 + 0x128,0x10);
          FUN_020a0fec(param_4 + 0xe8,0x40);
          func_0x020a5850(param_4 + 300,0x10,param_4 + 0x13c,0x11);
          FUN_020a0fec(param_4 + 300,*(undefined4 *)(param_4 + 0x13c));
          func_0x020a5850(param_4 + 0x140,0x10,param_4 + 0x150,0x12);
          FUN_020a0fec(param_4 + 0x140,*(undefined4 *)(param_4 + 0x150));
          iVar1 = func_0x020a6724();
        }
        if (iVar2 != 0) {
          return iVar2;
        }
      }
    }
    else {
      iVar1 = 0x101019;
    }
  }
  return iVar1;
}


