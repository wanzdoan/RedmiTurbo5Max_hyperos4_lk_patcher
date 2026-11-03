// File offset entry: 0xe5fc

void FUN_ffff0000b800e5fc(ulong param_1,undefined8 param_2,undefined8 param_3)

{
  ulong uVar1;
  bool bVar2;
  ulong uVar3;
  
  if ((param_1 & 2) != 0) {
    uVar3 = sctlr_el1;
    bVar2 = (uVar3 & 4) != 0;
    if (bVar2) {
      uVar3 = uVar3 & 0xfffffffffffffffb;
      sctlr_el1 = uVar3;
    }
    uVar1 = clidr_el1;
    FUN_ffff0000b800e478(bVar2,uVar3,param_3,(uVar1 >> 0x18 & 7) << 1);
  }
  if ((param_1 & 1) != 0) {
    uVar3 = sctlr_el1;
    sctlr_el1 = uVar3 & 0xffffffffffffefff;
    DataSynchronizationBarrier(3,3,0);
    IC_IALLU();
    DataSynchronizationBarrier(3,3,0);
    InstructionSynchronizationBarrier();
  }
  return;
}


