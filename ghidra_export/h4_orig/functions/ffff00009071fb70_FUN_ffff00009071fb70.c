// ffff00009071fb70  size=132  FUN_ffff00009071fb70

bool FUN_ffff00009071fb70(void)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff00009071f790();
  uVar4 = FUN_ffff00009077f250(&DAT_ffff0000907d3e3a);
  iVar2 = FUN_ffff00009077f348(uVar3,&DAT_ffff0000907d3e3a,uVar4);
  if (iVar2 == 0) {
    bVar1 = true;
  }
  else {
    uVar3 = FUN_ffff00009071f790();
    uVar4 = FUN_ffff00009077f250(&DAT_ffff0000907edab3);
    iVar2 = FUN_ffff00009077f348(uVar3,&DAT_ffff0000907edab3,uVar4);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


