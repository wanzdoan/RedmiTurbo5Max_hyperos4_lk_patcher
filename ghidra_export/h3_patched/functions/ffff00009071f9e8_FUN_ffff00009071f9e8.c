// ffff00009071f9e8  size=132  FUN_ffff00009071f9e8

bool FUN_ffff00009071f9e8(void)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  uVar3 = FUN_ffff00009071f608();
  uVar4 = FUN_ffff00009077ef88(&DAT_ffff0000907d3dfd);
  iVar2 = FUN_ffff00009077f080(uVar3,&DAT_ffff0000907d3dfd,uVar4);
  if (iVar2 == 0) {
    bVar1 = true;
  }
  else {
    uVar3 = FUN_ffff00009071f608();
    uVar4 = FUN_ffff00009077ef88(&DAT_ffff0000907ed92b);
    iVar2 = FUN_ffff00009077f080(uVar3,&DAT_ffff0000907ed92b,uVar4);
    bVar1 = iVar2 == 0;
  }
  return bVar1;
}


