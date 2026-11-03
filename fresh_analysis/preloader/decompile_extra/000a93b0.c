// File offset entry: 0xa93b0

/* WARNING: Removing unreachable block (ram,0x020a932c) */

undefined8 FUN_020a92b0(uint *param_1,uint param_2,uint param_3,int param_4)

{
  undefined4 uVar1;
  undefined4 uVar2;
  ulong uVar3;
  
  uVar1 = func_0x02091b64(param_4 * 1000);
  uVar2 = func_0x02091b28();
  do {
    uVar3 = func_0x02091c50(uVar2,uVar1);
    if ((uVar3 & 1) != 0) {
      return 0x7249;
    }
  } while ((*param_1 & param_2) != param_3);
  return 0;
}


