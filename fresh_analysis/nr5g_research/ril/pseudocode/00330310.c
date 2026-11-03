
/* RfxRadioCapabilityData::RfxRadioCapabilityData(void*, int) */

void __thiscall
RfxRadioCapabilityData::RfxRadioCapabilityData
          (RfxRadioCapabilityData *this,void *param_1,int param_2)

{
  undefined *puVar1;
  undefined8 *puVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  undefined8 uVar5;
  undefined8 uVar6;
  undefined8 uVar7;
  
  puVar1 = PTR_vtable_005d20f8;
  *(int *)(this + 0x10) = param_2;
  *(undefined **)this = puVar1 + 0x10;
  if (param_1 == (void *)0x0) {
    *(undefined8 *)(this + 8) = 0;
  }
  else {
    puVar2 = calloc(1,0x54);
    *(undefined8 **)(this + 8) = puVar2;
    if (puVar2 != (undefined8 *)0x0) {
      uVar3 = *(undefined8 *)((long)param_1 + 0x20);
      uVar5 = *(undefined8 *)((long)param_1 + 0x38);
      uVar4 = *(undefined8 *)((long)param_1 + 0x30);
      puVar2[5] = *(undefined8 *)((long)param_1 + 0x28);
      puVar2[4] = uVar3;
      puVar2[7] = uVar5;
      puVar2[6] = uVar4;
      uVar7 = *(undefined8 *)((long)param_1 + 0x40);
      uVar4 = *(undefined8 *)((long)param_1 + 8);
      uVar3 = *(undefined8 *)param_1;
      uVar6 = *(undefined8 *)((long)param_1 + 0x18);
      uVar5 = *(undefined8 *)((long)param_1 + 0x10);
      puVar2[9] = *(undefined8 *)((long)param_1 + 0x48);
      puVar2[8] = uVar7;
      *(undefined4 *)(puVar2 + 10) = *(undefined4 *)((long)param_1 + 0x50);
      puVar2[1] = uVar4;
      *puVar2 = uVar3;
      puVar2[3] = uVar6;
      puVar2[2] = uVar5;
    }
  }
  return;
}


