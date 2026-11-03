
/* RmcCatCommonRequestHandler::RmcCatCommonRequestHandler(int, int) */

void __thiscall
RmcCatCommonRequestHandler::RmcCatCommonRequestHandler
          (RmcCatCommonRequestHandler *this,int param_1,int param_2)

{
  undefined *puVar1;
  long lVar2;
  undefined6 uVar3;
  undefined6 uVar4;
  uint uVar5;
  int iVar6;
  char local_48 [6];
  char acStack_42 [2];
  char acStack_40 [8];
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  puVar1 = PTR_vtable_005d3510 + 0x10;
  *(int *)(this + 8) = param_1;
  *(int *)(this + 0xc) = param_2;
  *(undefined **)this = puVar1;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,(int *)&DAT_0025a470,0xb);
  RfxBaseHandler::registerToHandleEvent((RfxBaseHandler *)this,(int *)&DAT_0025a49c,2);
  *(undefined2 *)(this + 0x10) = 0;
  uVar3 = s_IMS_SUBMARINE_00240df6._0_6_;
  *(undefined8 *)(this + 0x18) = 0;
  *(undefined8 *)(this + 0x20) = 0;
  uVar4 = s_IMS_SUBMARINE_00240df6._0_6_;
  s_IMS_SUBMARINE_00240df6[0] = (char)uVar3;
  s_IMS_SUBMARINE_00240df6[1] = SUB61(uVar3,1);
  s_IMS_SUBMARINE_00240df6[2] = SUB61(uVar3,2);
  s_IMS_SUBMARINE_00240df6[3] = SUB61(uVar3,3);
  s_IMS_SUBMARINE_00240df6[4] = SUB61(uVar3,4);
  s_IMS_SUBMARINE_00240df6[5] = SUB61(uVar3,5);
  local_48[0] = s_IMS_SUBMARINE_00240df6[0];
  local_48[1] = s_IMS_SUBMARINE_00240df6[1];
  local_48[2] = s_IMS_SUBMARINE_00240df6[2];
  local_48[3] = s_IMS_SUBMARINE_00240df6[3];
  local_48[4] = s_IMS_SUBMARINE_00240df6[4];
  local_48[5] = s_IMS_SUBMARINE_00240df6[5];
  acStack_42[0] = s_IMS_SUBMARINE_00240df6[6];
  acStack_42[1] = s_IMS_SUBMARINE_00240df6[7];
  acStack_40[0] = s_IMS_SUBMARINE_00240df6[8];
  acStack_40[1] = s_IMS_SUBMARINE_00240df6[9];
  acStack_40[2] = s_IMS_SUBMARINE_00240df6[10];
  acStack_40[3] = s_IMS_SUBMARINE_00240df6[0xb];
  acStack_40[4] = s_IMS_SUBMARINE_00240df6[0xc];
  acStack_40[5] = s_IMS_SUBMARINE_00240df6[0xd];
  s_IMS_SUBMARINE_00240df6._0_6_ = uVar4;
  uVar5 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_48);
  RfxBaseHandler::logD
            ((char *)this,"RmcCatComReqHandler","query SUBMARINE support, %s=%d",local_48,
             (ulong)uVar5);
  if (uVar5 == 1) {
    *(undefined8 *)(this + 0x28) = 0;
    iVar6 = RfxRilUtils::getMajorSim();
    if (iVar6 + -1 == param_1) {
      initMdStatusQueryLoop(this);
    }
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


