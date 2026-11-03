
int FUN_ffff0000be0189f0(undefined8 param_1,undefined8 param_2)

{
  undefined8 *puVar1;
  long *plVar2;
  int iVar3;
  long lVar4;
  long local_1d8;
  long lStack_1d0;
  long local_1c8;
  undefined4 local_1c0;
  undefined1 auStack_1b8 [48];
  undefined1 auStack_188 [48];
  long local_158;
  long lStack_150;
  undefined4 local_148;
  undefined1 auStack_140 [48];
  undefined1 auStack_110 [48];
  undefined8 local_e0;
  undefined1 auStack_d8 [48];
  undefined1 auStack_a8 [48];
  undefined8 local_78;
  undefined8 local_70;
  undefined8 local_68;
  undefined8 local_60;
  undefined8 local_58;
  
  local_60 = 0;
  local_58 = 0;
  local_70 = 0;
  local_68 = 0;
  FUN_ffff0000be060ae0(&local_1d8,0,0x168);
  DAT_ffff0000be108808 = &local_70;
  DAT_ffff0000be108810 = &local_1d8;
  local_1d8 = FUN_ffff0000be017544();
  local_1c8 = local_1d8 + 0x1000;
  local_158 = local_1d8 + 0x201000;
  lStack_150 = local_1d8 + 0x202000;
  local_148 = 0;
  local_1c0 = 0;
  lStack_1d0 = local_1d8;
  FUN_ffff0000be01a4c4(auStack_1b8,0,1);
  FUN_ffff0000be01a4c4(auStack_140,0,1);
  FUN_ffff0000be01a4c4(auStack_188,1,1);
  FUN_ffff0000be01a4c4(auStack_110,1,1);
  FUN_ffff0000be01a4c4(auStack_d8,0,1);
  FUN_ffff0000be01a4c4(auStack_a8,0,1);
  puVar1 = DAT_ffff0000be108808;
  local_78 = 0;
  local_e0 = 0;
  *(undefined1 *)DAT_ffff0000be108808 = 1;
  puVar1[1] = 0;
  puVar1[2] = param_1;
  puVar1[3] = param_2;
  lVar4 = FUN_ffff0000be01a9dc
                    (s_write_storage_proc_ffff0000be0c2a8f,FUN_ffff0000be018784,0,0x1f,0x4000);
  if (lVar4 == 0) {
    FUN_ffff0000be05fd98(s_create_write_storage_proc_thread_ffff0000be0a4493);
    iVar3 = -0x69;
  }
  else {
    FUN_ffff0000be01aa0c();
    FUN_ffff0000be018860(param_1);
    FUN_ffff0000be01a590(DAT_ffff0000be108810 + 0x20,0xffffffff);
    FUN_ffff0000be01a590(DAT_ffff0000be108810 + 0x26,0xffffffff);
    plVar2 = DAT_ffff0000be108810;
    FUN_ffff0000be01a4fc(DAT_ffff0000be108810 + 10);
    FUN_ffff0000be01a4fc(plVar2 + 0x19);
    FUN_ffff0000be01a4fc(plVar2 + 4);
    FUN_ffff0000be01a4fc(plVar2 + 0x13);
    FUN_ffff0000be01a4fc(plVar2 + 0x20);
    FUN_ffff0000be01a4fc(plVar2 + 0x26);
    *plVar2 = 0;
    iVar3 = *(int *)((long)DAT_ffff0000be108810 + 0x164);
    if (-1 < iVar3) {
      iVar3 = (int)DAT_ffff0000be108810[0x2c];
    }
  }
  return iVar3;
}


